package controllers

import java.text.DecimalFormat

import play.api.mvc._

import scala.collection.{mutable}

object Application extends Controller {
  val db = new Db()
  val buffer = mutable.Buffer[Int]()

  def index = Action {
    Ok(views.html.main(db.getProducts))
  }


  def getHardwareProduct(idProduct: Int) = Action{
    println("вошли в geHardwareProduct")
    println("idProduct = " + idProduct)
    val options = db.getConfigurationOptions(idProduct)
    var countHardware = options.size
    Ok(views.html.products(options,idProduct,countHardware))
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


  def getPriceProduct(idProduct: Int, hardwareId1: Int, hardwareId2: Int, hardwareId3: Int, quantityProduct: Int)= Action{
    println("вошли в getPrice")
    println(idProduct)
    println(hardwareId1)
    println(hardwareId2)
    println(hardwareId3)
    val price = db.getPriceProduct(idProduct,hardwareId1,hardwareId2,hardwareId3)* quantityProduct
    val priceFormatted = new DecimalFormat("#0.00").format(price)
    println("priceFormatted = " + priceFormatted)

    Ok(views.html.price(priceFormatted))
  }

  def getPriceProducts(idProduct: Int, hardwareId1: Int, hardwareId2: Int, hardwareId3: Int, quantityProduct: Int)= Action{
    println("вошли в getPrices")
    println(idProduct)
    println(hardwareId1)
    println(hardwareId2)
    println(hardwareId3)
    val price = db.getPriceProduct(idProduct,hardwareId1,hardwareId2,hardwareId3)* quantityProduct
    val priceFormatted = new DecimalFormat("#0.00").format(price)
    println("price = " + price)

    Ok(""+priceFormatted+"")
  }



}