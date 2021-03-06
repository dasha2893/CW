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
    val tuples = SQL("select id_product, name from product")().map(row => (row[Int](1),row[String](2))).toList
    println("tuples = " + tuples)
    tuples

  }

  def getConfigurationOptions(idProduct: Int) = DB.withConnection { implicit c =>
     SQL(s"""select name_hardware, id_var_hw, model_hardware, name_parameter, value_parameter, unit_measure
                                  from build_product_variants('$idProduct')""")().
       map(row => (row[String](1),row[Int](2),row[String](3),row[String](4),row[Double](5).toString(), row[String](6))).
       groupBy(_._1).
       map(tuple => (tuple._1,tuple._2.map(t => (t._1, t._2, t._3, t._4.replace("unknown",""),t._5.replace("-9999.0",""),t._6.replace("unknown","")))))
  }

  def getListComponentProduct(idProduct: Int, idHardware1: Int, idHardware2: Int, idHardware3: Int) = DB.withConnection { implicit c =>
    SQL(s"""select bp.name_hardware, bp.amount, coalesce(b.name_hardware,'unknown'), coalesce(bp.price,0)
          from build_product_variant_ispolneniya('$idProduct','$idHardware1','$idHardware2','$idHardware3') as bp
          left join build_product_variant_ispolneniya('$idProduct','$idHardware1','$idHardware2','$idHardware3') as b
          on bp.id_par=b.id_var_hw
          where bp.name_hardware != b.name_hardware or bp.id_par ISNULL""")().map(row => (row[String](1),row[Int](2),row[String](3),row[Double](4))).
    groupBy(_._3).
    map(tuple => (tuple._1,tuple._2.map(t => (t._1,t._2,t._3.replace("unknown",t._1),t._4))))
  }

  def getPriceProduct(idProduct: Int, idHardware1: Int, idHardware2: Int, idHardware3: Int) = DB.withConnection { implicit c =>
    val price = SQL( s"""SELECT * from cost_product ('$idProduct','$idHardware1','$idHardware2','$idHardware3')""")().map(row => row[Double](1)).head
    price
  }

  def getConsumptionMaterials (idProduct: Int, idHardware1: Int, idHardware2: Int, idHardware3: Int) = DB.withConnection { implicit c =>
    val listMaterialConsumption = SQL( s"""SELECT * FROM amount_materials('$idProduct','$idHardware1','$idHardware2','$idHardware3')""")().
      map(row => (row[Int](1),row[String](2),row[Double](3),row[String](4))).toList
    listMaterialConsumption
  }
}
