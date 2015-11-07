package controllers

import java.text.{DecimalFormatSymbols, DecimalFormat}

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by ubuntu on 04.11.15.
 */
class Db {

  def collectProduct = DB.withConnection { implicit c =>
    SQL("""create or replace function build_product(id_product integer)returns integer AS
            $$
          declare
          i integer;
          begin
          with recursive building_product(name_hardware,id_hardware,amount,price) as(
          select hw.name_hardware, hw.id_hardware,pc.amount_elements,hw.price from product_composition pc left join hardware hw on (pc.fk_id_hardware=hw.id_hardware)
          where pc.fk_id_product=id_product
          union
          select hw.name_hardware,hc.fk_id_hardware,hc.amount_elements,hw.price from building_product bp inner join composite_complex_set hc on(bp.id_hardware=hc.fk_id_complex_hardware)
          left join hardware hw on(hc.fk_id_hardware=hw.id_hardware)
          )
          select sum(amount*price) from building_product into i;
          return i;
          end;
          $$ LANGUAGE plpgsql""")
  }


  def getProducts = DB.withConnection { implicit c =>
    println("Идём в базу")
    val tuples = SQL("select name_product,model_product from product")().map((row: Row) => (row[String](1),row[String](2))).
      groupBy(_._1).map((tuple: (String, Stream[(String, String)])) => (tuple._1,tuple._2.map(_._2).toList))
    println("tuples = " + tuples)
    tuples

  }

  def getIdProduct(product: String, model: String) = DB.withConnection { implicit c =>
    SQL(s"select id_product from product where name_product = '$product' and model_product='$model'")().map(row => row[Int](1)).head

  }

  def getPriceProduct (id:Int) = DB.withConnection { implicit c =>
    SQL(s"  select * from build_product($id)")().map(row => row[Double](1)).head
  }
//  def getValueParameters =  DB.withConnection { implicit c =>
//
//    println("Идём в базу")
//    val tuples = SQL(
//      """select p.id_parameter,p.name_parameter,u.name_unit_measure,h.value from parameters p
//        join hardware_parameters h
//        on p.id_parameter=h.fk_id_parameter
//        join unit_measures u
//        on p.fk_unit_measure=u.id_unit_measure""")().map((row: Row) => ParametersInfo(row[Int](1),row[String](2),row[String](3), row[String](3)))
//      .groupBy((_.id,_.name),).map()
//      .map((tuple: (Int, Array[(Int, String, String, String)])) => )
//    println("tuples = " + tuples)
//    tuples
//  }

}
