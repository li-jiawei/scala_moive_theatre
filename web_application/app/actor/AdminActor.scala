package actor


import java.sql.SQLTimeoutException

import actor.sentiment.{SentimentActor, StreamingActor}
import actor.ticket.ConsumerActor
import akka.actor.SupervisorStrategy.Restart
import akka.actor.{Actor, ActorLogging, Props}
import akka.actor._
import akka.routing.{DefaultResizer, RoundRobinPool}
import dao.MovieDao
import message.{SparkStreaming, StartSparkStreaming, _}
import play.api.Logger

import scala.concurrent.duration._


class AdminActor extends Actor with ActorLogging {
  var streamingActor: ActorRef = _
  var sentimentActor: ActorRef = _
  var consumerActor: ActorRef = _
  var movieDao:MovieDao = _

  val resizer = DefaultResizer(lowerBound = 2, upperBound = 15)

  override val supervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5, withinTimeRange = 1.minute) {
    case e:SQLTimeoutException => Restart
    case _: Exception => Restart
  }

  implicit val ec = context.dispatcher

  override def preStart(): Unit = {
    super.preStart()
    log.info("Starting AdminActor")
    log.info("Creating StreamingActor")
    Logger.info("Creating StreamingActor")
    streamingActor = context.actorOf(Props[StreamingActor], name = "streamingActor")
    sentimentActor = context.actorOf(Props[SentimentActor], name = "sentimentActor")
  }

  override def receive = {
    case TransferDao(dao) =>
      println("Accepting dao")
      movieDao = dao
      consumerActor = context.actorOf(RoundRobinPool(5, Some(resizer), supervisorStrategy).props(ConsumerActor.props(movieDao)), "router2")
                      //context.actorOf(ConsumerActor.props(movieDao), name = "consumerActor")

    case m @ Buy(movieName, sid) =>
      println("Accepting Buy message")
      val original = sender()
      consumerActor ! Buy2(sid, original)


    case "test" =>
      println("testing  shows")

    case message @ StartSparkStreaming(_) =>
      Logger.info("forwarding the message to streamingActor")
      streamingActor ! message

    case message @ SparkStreaming(_, _) =>
      Logger.info("forwarding the message to streamingActor")
      streamingActor ! message
  }

}

object AdminActor {

  def props = Props(classOf[AdminActor])
}
