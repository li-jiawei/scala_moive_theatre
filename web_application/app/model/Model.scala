package model

import java.sql.Timestamp

import play.api.Play
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
import slick.profile.SqlProfile.ColumnOption.NotNull

import scala.concurrent.ExecutionContext.Implicits.global

object Model {

  case class Movie(id: Option[Int], name: String)

  case class Show(sid: Option[Int], movieName: String, remainingTicket: Int, showTime: Timestamp)

  case class Order(oid: String, sid: Int)


  class MovieTableDef(tag: Tag) extends Table[Movie](tag, "movies") {
    def mid = column[Int]("mid", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name", NotNull)

    def * = (mid.?, name) <> (Movie.tupled, Movie.unapply)
  }

  lazy val movies = TableQuery[MovieTableDef]

  class ShowTableDef(tag: Tag) extends Table[Show](tag, "shows") {
    def sid = column[Int]("sid", O.PrimaryKey, O.AutoInc)

    def movieName = column[String]("movie_name")

    def remainingTicket = column[Int]("remaining_ticket")

    def showTime = column[Timestamp]("show_time")

    def movie = foreignKey("MOVIE_NAME_FK", movieName, movies)(_.name, onUpdate = ForeignKeyAction.NoAction, onDelete = ForeignKeyAction.NoAction)

    def * = (sid.?, movieName, remainingTicket, showTime) <> (Show.tupled, Show.unapply)

  }

  lazy val shows = TableQuery[ShowTableDef]

  class OrderTableDef(tag: Tag) extends Table[Order](tag, "orders"){
    def oid = column[String]("oid", O.PrimaryKey)

    def sid = column[Int]("sid", NotNull)

    def show = foreignKey("SHOW_ID_FK", sid, shows)(_.sid)

    def * = (oid, sid) <> (Order.tupled, Order.unapply)
  }

  lazy val orders = TableQuery[OrderTableDef]
}