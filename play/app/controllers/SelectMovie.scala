package controllers
import com.google.inject.Inject
import dao.MovieDao

import scala.io.Source
import play.api._
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by ling on 16/11/22.
  */
class SelectMovie @Inject() (movieDao: MovieDao) extends Controller {

  def selectmovie = Action.async {

//    val args = "movieList"
//
//    var movieList  = "" :: Nil
//    for (line <- Source.fromFile(args).getLines) {
//      movieList = line.toString::movieList
//    }
//    movieList = movieList.take(movieList.size-1)

    val movieList = movieDao.listAll
    movieList
      .map{ list => list.map(_.name)}
      .map(list => Ok(views.html.selectM(list)))
  }

}
