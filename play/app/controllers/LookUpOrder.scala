package controllers


import play.api.mvc._
import java.sql.{DriverManager, ResultSet}


/**
  * Created by ling on 16/11/22.
  */
class LookUpOrder extends Controller {


  def lookuporder(orderid: String) = Action {
    val conn_str = "jdbc:postgresql://localhost:5432/postgres"
    val conn = DriverManager.getConnection(conn_str, "postgres", "0403")
    try {

      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
//      val queryS = "SELECT * FROM orderlist WHERE orderid =" + orderid + " ; "
//      val order = statement.executeQuery(queryS)
//      order.next
//      val moviename = order.getString(3).replace("_"," ")
//      val queryShowtime = "SELECT showtime FROM timelist where movieid="+order.getString(1)+";"
//              val showtime = statement.executeQuery(queryShowtime)
//              showtime.next()
//              val showT = showtime.getString(1)
      val movieid = orderid.substring(0,10)
      val query = "SELECT * FROM timelist where movieid="+movieid+";"
      val order = statement.executeQuery(query)
      order.next
      Ok (views.html.lookUpOrder(order.getString(1).replace("_"," "), order.getString(4)))

    } finally {
      conn.close
    }





  }
}