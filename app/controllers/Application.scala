package controllers

import java.io.Serializable
import java.util

import play.api._
import play.api.mvc._

import scala.collection.{immutable, mutable}

object Application extends Controller {
  var product=""
  var idProduct= 0
  val db = new Db()


  def index = Action {
    Ok(views.html.main(db.getProducts))
  }


  def geHardwareProduct(pr: String) = Action{
    println("вошли в geHardwareProduct")
    println("product = " + pr)
    product=pr
    idProduct = db.getIdProduct(product)
    println(idProduct)
    val options = db.getConfigurationOptions(idProduct)


    Ok(views.html.products(options))
  }

//  def getPrice(pr: String, mod: String, quantityProduct:Int)= Action{
//    db.collectProduct
//    val price= db.getPriceProduct(idProduct)*quantityProduct
//    println("price = " + price)
//
//    Ok(""+price+"")
//  }



}