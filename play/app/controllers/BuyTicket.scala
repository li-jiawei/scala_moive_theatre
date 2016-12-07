package controllers

import java.sql.{DriverManager, ResultSet}
import java.util.Calendar

import com.google.inject.Inject
import dao.MovieDao
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ling on 16/11/22.
  */

class BuyTicket @Inject() (movieDao: MovieDao) extends Controller {


  def buyticket(moviename:String, sid: String) = Action.async {
    val orderId = movieDao.buyTicket(sid.toInt)
    orderId.map(oid => Ok(views.html.ticketResult(moviename, oid)))

    //    val orderid = postgres_jdbc.main(movieid, moviename)
//    orderid match {
//      case "0" => Ok(views.html.ticketResultFail("failed"))
//      case _ => Ok(views.html.ticketResult(moviename, orderid.toString))
//    }
  }


  object postgres_jdbc {
    val conn_str = "jdbc:postgresql://localhost:5432/testdb"

    def main(movieid: String,moviename:String): String = {
      var time = (Calendar.getInstance().getTimeInMillis%100000000).toInt
      val orderid = movieid.toString+time.toString
      val conn = DriverManager.getConnection(conn_str, "postgres", "lijiawei")
      try {

        val statement = conn.createStatement()

        val queryOrder = "INSERT INTO orderlist VALUES( '"+orderid+"');"
        val queryUpdate =  "UPDATE timelist SET remainticket =remainticket-1 WHERE movieid ="+movieid+";"

        statement.executeUpdate(queryUpdate)
        statement.executeUpdate(queryOrder)


        return orderid
      } catch{
        case ex: org.postgresql.util.PSQLException => {
          System.out.print(ex)
          return "0"
        }
      } finally{
        conn.close
      }
    }
  }

}