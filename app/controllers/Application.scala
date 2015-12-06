package controllers

import play.api.mvc._

import scala.collection.{mutable}

object Application extends Controller {
  val db = new Db()
  val buffer = mutable.Buffer[Int]()

  def index = Action {
    Ok(views.html.main(db.getProducts))
  }


  def getHardwareProduct(product: String) = Action{
    println("вошли в geHardwareProduct")
    println("product = " + product)
    val idProduct = db.getIdProduct(product)
    println(idProduct)
    val options = db.getConfigurationOptions(idProduct)
    Ok(views.html.products(options,idProduct))
  }


  def getAllCompositionProduct(idProduct: Int, hardwareId1: Int,hardwareId2: Int,hardwareId3: Int) = Action {
    println("вошли в getAllCompositionProduct")
    println(idProduct)
    println(hardwareId1)
    println(hardwareId2)
    println(hardwareId3)
    val listComponentProduct = db.getListComponentProduct(idProduct, hardwareId1,hardwareId2,hardwareId3)
    Ok(views.html.composition(listComponentProduct))
  }
//  def getPrice(pr: String, mod: String, quantityProduct:Int)= Action{
//    db.collectProduct
//    val price= db.getPriceProduct(idProduct)*quantityProduct
//    println("price = " + price)
//
//    Ok(""+price+"")
//  }



}