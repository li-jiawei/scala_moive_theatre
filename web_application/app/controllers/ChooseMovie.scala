package controllers

import play.api.mvc._
import play.api._

class ChooseMovie extends Controller {


  def choosemovie(movieName:String)= Action {

    Ok(views.html.moviemain(movieName))
  }
}