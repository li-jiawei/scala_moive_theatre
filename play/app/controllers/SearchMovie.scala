package controllers


import play.api._
import play.api.mvc._

/**
  * Created by ling on 16/11/22.
  */
class SearchMovie extends Controller {


  def searchmovie(searchKey:String)= Action {

    Ok(views.html.searchM("search movie"+searchKey))
  }
}