package controllers
import com.google.inject.Inject
import dao.MovieDao

import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global


class SelectMovie @Inject() (movieDao: MovieDao) extends Controller {

  def selectmovie = Action.async {
    val movieList = movieDao.listAll
    movieList
      .map{ list => list.map(_.name)}
      .map(list => Ok(views.html.selectM(list)))
  }
}
