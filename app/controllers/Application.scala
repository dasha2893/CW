package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {
  var product=""
  var model=""
  var idProduct= 0
  val db = new Db()


  def index = Action {
    Ok(views.html.main(db.getProducts))
  }


  def getData(pr: String, mod: String) = Action{
    println("model = " + mod)
    println("product = " + pr)
    product=pr
    model=mod
    db.collectProductParameters
    val productParameters = db.getProductParameters(idProduct)
    println("productParameters = " +  productParameters.getClass)
    println("productParameters = " + productParameters)
    Ok(views.html.products(product,model,productParameters))
  }

  def getPrice(pr: String, mod: String, quantityProduct:Int)= Action{
    idProduct = db.getIdProduct(pr,mod)
    println("idProduct = " + idProduct)
    db.collectProduct
    val price= db.getPriceProduct(idProduct)*quantityProduct
    println("price = " + price)
    Ok(""+price+"")
  }



}