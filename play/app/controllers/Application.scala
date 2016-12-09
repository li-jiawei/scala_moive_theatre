package controllers

import javax.inject.Inject

import actor.sentiment.WebsocketActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import message.{ChannelBroadcast, SparkStreaming}
import play.api._
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.iteratee.{Concurrent, Iteratee}
import play.api.libs.json.JsValue
import play.api.libs.streams.ActorFlow
import play.api.mvc._
import slick.driver.JdbcProfile

import scala.concurrent.Future

class Application @Inject() (implicit system: ActorSystem, materializer: Materializer, conf: Configuration) extends Controller {
//  val dbConfig = dbConfigProvider.get[JdbcProfile]

  implicit val ec = system.dispatcher

  val adminActor = system.actorSelection("akka://application/user/adminActor")
  //  adminActor ! "test"
  //  adminActor ! StartSparkStreaming(conf)
  adminActor ! SparkStreaming(true, conf)

  def stop() = Action {
    adminActor ! SparkStreaming(false, conf)
    Ok("goood")
  }


  def test = WebSocket.using[JsValue]{ request =>
    // Concurrent.broadcast returns (Enumerator, Concurrent.Channel)
    val (out, channel) = Concurrent.broadcast[JsValue]

    val sentimentActor = system.actorSelection("akka://application/user/adminActor/sentimentActor")
    // log the message to stdout and send response back to client
    val in = Iteratee.foreach[JsValue]{ m =>
      println(m.toString() + "haha")
    }
    sentimentActor ! ChannelBroadcast(channel)

    // the Enumerator returned by Concurrent.broadcast subscribes to the channel and will
    // receive the pushed messages
    //        channel push("I received your message: " + msg)
    //    out run in
    (in,out)
  }

  def test3 = WebSocket.accept[JsValue, JsValue]{ implicit request =>
    ActorFlow.actorRef(out => WebsocketActor.props(out))
  }

  def test2 = WebSocket.using[String] { request =>
    val (chatEnumerator, chatChannel) = Concurrent.broadcast[String]
    val newEnum = chatEnumerator.map{ s =>
      println("在chatEnumertor里面")
      s * 2
    }
    chatChannel.push("inside chatclient2")
    val chatClient1 = Iteratee.foreach[String](m => println("Client 1: " + m))
    val chatClient2 = Iteratee.foreach[String]{ m =>
      println("Client 2: " + m)

    }
    //    chatEnumerator |>>> chatClient1
    //    chatEnumerator |>>> chatClient2
    //    newEnum |>>> chatClient2
    //    chatChannel.push("Hello world!")

    (chatClient2, newEnum)
  }

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}