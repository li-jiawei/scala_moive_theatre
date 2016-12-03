package controllers

import play.api.mvc._
import java.sql.{DriverManager, ResultSet}

import java.util.Calendar

import scala.io.Source

/**
  * Created by ling on 16/11/22.
  */

class Ticket extends Controller {

  val currentTime = Calendar.getInstance().getTime()

  def ticket(movieName: String) = Action {
    Ok(views.html.ticket(movieName,postgres_jdbc.main(movieName)))
  }


  object postgres_jdbc {
    val conn_str = "jdbc:postgresql://localhost:5432/postgres"

    def main(movieName: String): List[String] = {
      var listM = " " :: Nil
      val conn = DriverManager.getConnection(conn_str, "postgres", "0403")
      try {
        val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
        val queryS = "SELECT * FROM timelist WHERE moviename = '" + movieName + "' and showtime > '" + currentTime + "' and remainticket >0 ;"
        val rs = statement.executeQuery(queryS)
        var columnCount = rs.getMetaData().getColumnCount();
        while (rs.next) {
          for (i <- 1 to columnCount) {
            listM = rs.getString(i).replace(" ","_") :: listM;
          }
        }
        listM.take(listM.size - 1)

      } finally {
        conn.close
      }
    }
  }

}