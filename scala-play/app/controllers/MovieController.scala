package controllers

import javax.inject._

import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future, Promise}

@Singleton
class MovieController @Inject() extends Controller{

  def selectmovie = Action {
    Ok(views.html.index("remain doing"))
  }

}
