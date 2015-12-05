package controllers

import java.text.{DecimalFormatSymbols, DecimalFormat}

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by ubuntu on 04.11.15.
 */
class Db {

  def getProducts = DB.withConnection { implicit c =>
    println("Идём в базу")
    val tuples = SQL("select name from product")().map(row => row[String](1)).toList
    println("tuples = " + tuples)
    tuples

  }

  def getIdProduct(product: String) = DB.withConnection { implicit c =>
    SQL(s"select id_product from product where name = '$product'")().map(row => row[Int](1)).head
  }

  def getConfigurationOptions(idProduct: Int) = DB.withConnection { implicit c =>
     SQL(s"""select name_hardware, id_var_hw, model_hardware, name_parameter, value_parameter, unit_measure
                                  from build_product_variants('$idProduct')""")().
       map(row => (row[String](1),row[Int](2),row[String](3),row[String](4),row[Double](5).toString(), row[String](6))).
       groupBy(_._1).
       map(tuple => (tuple._1,tuple._2.map(t => (t._1, t._2, t._3, t._4.replace("unknown",""),t._5.replace("-9999.0",""),t._6.replace("unknown","")))))
  }

//
//  def getPriceProduct (id:Int) = DB.withConnection { implicit c =>
//    SQL(s"  select * from build_product($id)")().map(row => row[Double](1)).head
//  }
//
//  def getProductParameters(idProduct:Int) = DB.withConnection { implicit c =>
//    println("idProduct = " + idProduct)
//    val list = SQL(s"SELECT * from build_product_parameters('$idProduct')")().map(row => (row[String](1), row[String](2), row[String](3), row[String](4))).toList
//    list.foreach(println)
//    list
//
//  }


}
