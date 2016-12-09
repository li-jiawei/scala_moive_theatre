package message

import akka.actor.ActorRef
import play.api.Configuration
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsValue

case class StartSparkStreaming(conf: Configuration)

case class Sentiment(s: Double, count: Long)

case class SparkStreaming(start: Boolean, conf: Configuration)

case class ChannelBroadcast(channel: Concurrent.Channel[JsValue])

case class StartSubsribe(out: ActorRef)

case class StopSubscribe(out: ActorRef)