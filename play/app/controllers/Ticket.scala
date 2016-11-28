package controllers

import play.api.mvc._

import scala.io.Source

/**
  * Created by ling on 16/11/22.
  */
class Ticket extends Controller {

  def ticket(movieName: String) = Action {
    Ok(views.html.ticket(movieName))
  }

}
