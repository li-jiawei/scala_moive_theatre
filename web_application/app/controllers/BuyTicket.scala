package controllers

import java.sql.{DriverManager, ResultSet}
import java.util.Calendar

import akka.actor.ActorSystem
import akka.util.Timeout
import com.google.inject.Inject
import dao.MovieDao
import play.api.mvc._
import message._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import akka.pattern.ask


class BuyTicket @Inject() (movieDao: MovieDao, system: ActorSystem) extends Controller {
  val adminActor = system.actorSelection("akka://application/user/adminActor")
  adminActor ! TransferDao(movieDao)

  implicit val timeout: Timeout = Timeout(10.seconds)

  def buyticket(moviename:String, sid: String) = Action.async {
    val result = adminActor ? Buy(moviename, sid.toInt)
    result.mapTo[String].map{ oid =>
      Ok(views.html.ticketResult(moviename, oid))
    }.recover{
      case e:Exception =>
        Ok(views.html.unableToBuyTicket("Failed \n" + e.getMessage))
    }
//    val orderId = Await.result[String](movieDao.buyTicket(sid.toInt), 1.second)
//    Ok(views.html.ticketResult(moviename, orderId))
//    orderId
//      .map{oid =>
//        println("----------safe here-----------")
//        Ok(views.html.ticketResult(moviename, oid))}
//      .recover()

    //    val orderid = postgres_jdbc.main(movieid, moviename)
//    orderid match {
//      case "0" => Ok(views.html.ticketResultFail("failed"))
//      case _ => Ok(views.html.ticketResult(moviename, orderid.toString))
//    }
  }
}