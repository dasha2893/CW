package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  val db = new Db()

  def index = Action {
    Ok(views.html.main(db.getParameters.toList))
  }


  def getData(value: String) = Action{
    println("value = " + value)
    Ok("hi")
  }

}