package controllers

import javax.inject.Inject

import play.api._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc._
import slick.driver.JdbcProfile

import scala.concurrent.Future

class Application extends Controller {
//  val dbConfig = dbConfigProvider.get[JdbcProfile]

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}