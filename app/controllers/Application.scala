package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  val db = new Db()


  def index = Action {
    Ok(views.html.main(db.getProducts))
  }


  def getData(product: String, model: String) = Action{
    println("model = " + model)
    println("product = " + product)
    Ok(views.html.products(product,model))
  }

  def getPrice(product: String, model: String, quantityProduct:Int)= Action{
    val idProduct = db.getIdProduct(product,model)
    db.collectProduct
    val price= db.getPriceProduct(idProduct)*quantityProduct
    Ok(""+price+"")
  }

}