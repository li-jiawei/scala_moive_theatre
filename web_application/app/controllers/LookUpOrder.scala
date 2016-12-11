package controllers


import play.api.mvc._
import java.sql.{DriverManager, ResultSet}

import com.google.inject.Inject
import dao.MovieDao

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ling on 16/11/22.
  */
class LookUpOrder @Inject() (movieDao: MovieDao) extends Controller {


  def lookuporder(orderId: String) = Action.async {
    val info = movieDao.lookupOrder(orderId)
    info.map{ list =>
      val (oid, movieName, showTime) = list.head
      Ok(views.html.lookUpOrder(oid, movieName, showTime))
    }
  }
}