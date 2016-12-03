package controllers


import play.api.mvc._
import java.sql.{DriverManager, ResultSet}


/**
  * Created by ling on 16/11/22.
  */
class LookUpOrder extends Controller {


  def lookuporder(orderid: String) = Action {
    val moviename = ""
    val showtime = ""

    val conn_str = "jdbc:postgresql://localhost:5432/postgres"
    val conn = DriverManager.getConnection(conn_str, "postgres", "0403")
    try {
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)
      val queryS = "SELECT * FROM orderlist WHERE orderid =" + orderid + " ; "
      val order = statement.executeQuery(queryS)
      order.next
      Ok (views.html.lookUpOrder(order.getString(3).replace("_"," "), order.getString(4)))

    } finally {
      conn.close
    }





  }
}