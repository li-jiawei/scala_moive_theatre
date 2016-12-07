package controllers

import play.api.mvc._
import java.sql.{DriverManager, ResultSet}
import java.util.Calendar

import com.google.inject.Inject
import dao.MovieDao
import scala.concurrent.ExecutionContext.Implicits.global

import scala.io.Source

/**
  * Created by ling on 16/11/22.
  */

class Ticket @Inject() (movieDao: MovieDao) extends Controller {

  val currentTime = Calendar.getInstance().getTime()

  def ticket(movieName: String) = Action.async {
//    Ok(views.html.ticket(movieName,postgres_jdbc.main(movieName)))
    movieDao
      .listShowsOfMovie(movieName)
      .map(_.unzip)
      .map { case (sids, showTimes) =>
        Ok(views.html.ticket(movieName, sids, showTimes))
      }

  }

//
//  object postgres_jdbc {
//    val conn_str = "jdbc:postgresql://localhost:5432/testdb"
//
//    def main(movieName: String): List[String] = {
//      var listM = " " :: Nil
//      val conn = DriverManager.getConnection(conn_str, "postgres", "lijiawei")
//      try {
//        val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
//        val queryS = "SELECT * FROM timelist WHERE moviename = '" + movieName + "' and showtime > '" + currentTime + "' and remainticket >0 ;"
//        val rs = statement.executeQuery(queryS)
//        var columnCount = rs.getMetaData().getColumnCount();
//        while (rs.next) {
//          for (i <- 1 to columnCount) {
//            listM = rs.getString(i).replace(" ","_") :: listM;
//          }
//        }
//        listM.take(listM.size - 1)
//
//      } finally {
//        conn.close
//      }
//    }
//  }

}