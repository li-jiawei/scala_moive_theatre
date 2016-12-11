package actor.sentiment

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import message._
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsValue

class SentimentActor extends Actor with ActorLogging{
  var zeros: Long = 0
  var ones: Long = 0
  var twos: Long = 0
  var threes: Long = 0
  var fours: Long = 0

  var channel: Option[Concurrent.Channel[JsValue]] = None
  var subscribers: Set[ActorRef] = Set()

  override def receive = {
    case Sentiment(score, count) => {
      score match {
        case x if x == 4.0 => fours += count
        case x if x == 3.0 => threes += count
        case x if x == 2.0 => twos += count
        case x if x == 1.0 => ones += count
        case x if x == 0.0 => zeros +=  count
        case x: Double => println("something wrong")
      }
      import play.api.libs.json.{JsValue, Json}
      val json: JsValue = Json.obj(
        "zero" -> zeros,
        "one" -> ones,
        "two" -> twos,
        "three" -> threes,
        "four" -> fours
      )
      println(json.toString())

      // code that use iteratee, enumerator and channel
//      channel.foreach{ ch =>
//        ch.push(json)
//      }
//      println(channel.isEmpty)

      subscribers.foreach{ out =>
        out ! json
      }
    }
    case ChannelBroadcast(c) => channel = Option(c)

    case StartSubsribe(out) => subscribers += out

    case StopSubscribe(out) => subscribers -= out

    case "test" =>
      println("testing  shows")
  }

}

object SentimentActor {
  def props = Props(classOf[SentimentActor])
}