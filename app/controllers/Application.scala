package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  var product=""
  var idProduct= 0
  val db = new Db()


  def index = Action {
    Ok(views.html.main(db.getProducts))
  }


  def geHardwareProduct(pr: String) = Action{
    println("product = " + pr)
    product=pr
    idProduct = db.getIdProduct(product)
    val options: Unit = db.getConfigurationOptions(idProduct)

    Ok(views.html.products())
  }

//  def getPrice(pr: String, mod: String, quantityProduct:Int)= Action{
//    db.collectProduct
//    val price= db.getPriceProduct(idProduct)*quantityProduct
//    println("price = " + price)
//
//    Ok(""+price+"")
//  }



}