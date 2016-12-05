package controllers

import java.sql.{DriverManager, ResultSet}
import java.util.Calendar

import play.api.mvc._

/**
  * Created by ling on 16/11/22.
  */

class BuyTicket extends Controller {


  def buyticket(movieid: String,moviename:String) = Action {
    val orderid = postgres_jdbc.main(movieid, moviename)
    orderid match {
      case "0" => Ok(views.html.ticketResultFail("failed"))
      case _ => Ok(views.html.ticketResult(moviename, orderid.toString))
    }
  }


  object postgres_jdbc {
    val conn_str = "jdbc:postgresql://localhost:5432/postgres"

    def main(movieid: String,moviename:String): String = {
      var time = (Calendar.getInstance().getTimeInMillis%100000000).toInt
      val orderid = movieid.toString+time.toString
      val conn = DriverManager.getConnection(conn_str, "postgres", "0403")
      try {

        val statement = conn.createStatement()
//        val queryShowtime = "SELECT showtime FROM timelist where movieid="+movieid+";"
//        val showtime = statement.executeQuery(queryShowtime)
//        showtime.next()
//        val showT = showtime.getString(1)


//        val showT="2016-01-01 12:00:00"
//        val queryOrder = "INSERT INTO orderlist VALUES( "+movieid+", 'User', '"+ moviename+"','"+showT + "', "+orderid+");"
//        val queryUpdate =  "UPDATE timelist SET remainticket =remainticket-1 WHERE movieid ="+movieid+";"


        val queryOrder = "INSERT INTO orderlist1 VALUES( '"+orderid+"');"
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