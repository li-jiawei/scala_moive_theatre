package controllers

import java.io.PrintWriter

import play.api.mvc._

import scala.io.Source
import scala.io._
import java.io.{File, PrintWriter}

/**
  * Created by ling on 16/11/22.
  */
class Tweet extends Controller {

  def tweet(movieName: String) = Action { implicit request =>
    Ok(views.html.tweet(movieName))
  }

}
