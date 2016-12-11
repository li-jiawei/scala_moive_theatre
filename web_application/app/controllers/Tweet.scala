package controllers


import play.api.mvc._


class Tweet extends Controller {

  def tweet(movieName: String) = Action { implicit request =>
    Ok(views.html.tweet(movieName))
  }

}
