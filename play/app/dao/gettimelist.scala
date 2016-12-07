package dao

import java.sql.{DriverManager, ResultSet}




/**
  * Created by ling on 16/12/2.
  */
class gettimelist {


  object postgres_jdbc {
    val conn_str = "jdbc:postgresql://localhost:5432/postgres"
    //classOf[org.postgresql.Driver]

    def main(args: Array[String]) {
      //classOf[org.postgresql.Driver]

      val conn = DriverManager.getConnection(conn_str, "postgres", "0403")
      try {
        // Configure to be Read Only
        val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

        // Execute Query
        val rs = statement.executeQuery("SELECT * FROM timelist")
        var columnCount = rs.getMetaData().getColumnCount();
        // Iterate Over ResultSet
        while (rs.next) {
          for (i <- 1 to columnCount) {
            System.out.print(rs.getString(i) + "\t");
          }
          System.out.println();
        }
      } finally {
        conn.close
      }
    }




  }










}
