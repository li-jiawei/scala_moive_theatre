package dao


import java.sql.Timestamp

import com.google.inject._
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import model.Model._

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class MovieDao @Inject() (dbProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbProvider.get[JdbcProfile]


  def listAll: Future[Seq[Movie]] = {
    dbConfig.db.run(movies.result)
  }

  def listShowsOfMovie(movieName: String): Future[Seq[(Int, Timestamp)]] = {
    val currentTime = new java.sql.Timestamp(System.currentTimeMillis())
    dbConfig.db.run{
      shows
        .filter(table =>
          table.movieName === movieName &&
          table.showTime > currentTime &&
          table.remainingTicket > 0)
        .map(table => (table.sid, table.showTime))
        .sortBy(_._1)
        .result
    }
  }

  def buyTicket(sid: Int): Future[String] = {
    val uuid = java.util.UUID.randomUUID.toString
//    println(uuid)
    val updateTicketAmount = sqlu"""UPDATE shows SET remaining_ticket=(remaining_ticket - 1) WHERE sid=#$sid;"""
    val insertThisOrder = sqlu"""INSERT INTO orders(oid, sid) values ('#$uuid', #$sid);"""
//    val GetOrderId = orders
//    println(updateTicketAmount.toString)
//    val composedAction = for {
//      u <- updateTicketAmount
//      i <- insertThisOrder
//    } yield i
//    val operation = dbConfig.db.run{
//      composedAction.transactionally
//    }
    val operation = dbConfig.db.run{
      updateTicketAmount.andThen(insertThisOrder).transactionally
    }
    operation.map(oper => uuid)
  }

  def lookupOrder(orderId: String): Future[Seq[(String, String, Timestamp)]] = {
    val query = sql"""SELECT oid, movie_name, show_time FROM orders, shows WHERE orders.sid=shows.sid and oid='#$orderId'"""
    dbConfig.db.run{
      query.as[(String, String, Timestamp)]
    }
  }
}
