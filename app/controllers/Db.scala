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

  def collectProductParameters = DB.withConnection { implicit c =>
    SQL("""create or replace function build_product_parameters(id_product integer)returns table (name_hardware character varying,name_parameter character varying,value_parameter character varying, unit_measure character varying) AS
            $$
          begin
          return query
          with recursive building_product(name_hardware,id_hardware,amount,price) as(
          select hw.name_hardware, hw.id_hardware,pc.amount_elements,hw.price from product_composition pc left join hardware hw on (pc.fk_id_hardware=hw.id_hardware)
          where pc.fk_id_product=id_product
          union
          select hw.name_hardware,hc.fk_id_hardware,hc.amount_elements,hw.price from building_product bp inner join composite_complex_set hc on(bp.id_hardware=hc.fk_id_complex_hardware)
          left join hardware hw on(hc.fk_id_hardware=hw.id_hardware)
          )
          select bp.name_hardware,par.name_parameter,hp.value,um.designation from building_product bp inner join hardware_parameters hp on (bp.id_hardware=hp.fk_id_hardware)
          left join parameters par on(hp.fk_id_parameter=par.id_parameter)
          left join unit_measures um on(par.fk_unit_measure=um.id_unit_measure);
          end;
          $$ LANGUAGE plpgsql;""")
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

  def getProductParameters(idProduct:Int) = DB.withConnection { implicit c =>
    println("idProduct = " + idProduct)
    val list = SQL(s"SELECT * from build_product_parameters('$idProduct')")().map(row => (row[String](1), row[String](2), row[String](3), row[String](4))).toList
    list.foreach(println)
    list

  }


}
