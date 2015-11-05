package controllers

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by ubuntu on 04.11.15.
 */
class Db {

  def getParameters = DB.withConnection { implicit c =>
    println("Идём в базу")
    SQL("SELECT  name_parameter FROM parameters")().map(row => row[String]("1"))
  }

}
