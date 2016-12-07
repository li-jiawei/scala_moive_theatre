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

  def tweet(movieName: String) = Action {
    var scoreList  = 0:: Nil
    for (line <- Source.fromFile(movieName).getLines)
      scoreList = line.toString.toInt::scoreList //  position!!0 1 2 3 4
    scoreList = scoreList.take(scoreList.size-1)
    scoreList = scoreList.reverse




    /**
      *  movieName = This_Is_Test_Name
      *  Movie Key Word:  movieName.replace("_"," ")
      *  Movie Score : scoreList(0) 0 score number
      *  Movie Score : scoreList(1) 1 score number
      * */





    val writer = new PrintWriter(new File(movieName))
    for(score <-scoreList)
      writer.println(score)
    writer.close()
    Ok(views.html.tweet(movieName,scoreList(0),scoreList(1),scoreList(2),scoreList(3),scoreList(4)))
  }

}
