package message

import akka.actor.ActorRef
import dao.MovieDao
import play.api.Configuration
import play.api.libs.iteratee.Concurrent
import play.api.libs.json.JsValue

case class Buy(movieName: String, sid: Int)
case class TransferDao(movieDao: MovieDao)

case class Buy2(sid: Int, out: ActorRef)
case class ReturnOrder(orderId: String)