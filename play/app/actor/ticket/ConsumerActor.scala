package actor.ticket

import akka.actor.{Actor, ActorLogging, Props}
import com.google.inject.Inject
import dao.MovieDao
import message._


/**
  * Created by Jiawei on 12/8/16.
  */
class ConsumerActor @Inject() (movieDao: MovieDao) extends Actor with ActorLogging{

  implicit val ec = context.dispatcher

  override def preStart(): Unit = {

  }

  override def receive = {
    case Buy2(sid, out) => {
      println("Accepting Buy2 message")
      val result = movieDao.buyTicket(sid)
      result.map{ oid =>
        println("received oid: " + oid)
        println(out)
        out ! oid
      }
    }


    case "test" => println("Test succeed")
  }
}

object ConsumerActor {

  def props(movieDao: MovieDao) = Props(new ConsumerActor(movieDao))

}