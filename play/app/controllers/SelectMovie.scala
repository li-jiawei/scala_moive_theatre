package controllers
import scala.io.Source
import play.api._
import play.api.mvc._

/**
  * Created by ling on 16/11/22.
  */
class SelectMovie extends Controller {

  def selectmovie = Action {

    val args = "movieList"

    var movieList  = "" :: Nil
    for (line <- Source.fromFile(args).getLines) {
      movieList = line.toString::movieList
    }
    movieList = movieList.take(movieList.size-1)

    Ok(views.html.selectM(movieList))
  }

}
