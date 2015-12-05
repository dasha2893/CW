package controllers

import java.text.{DecimalFormatSymbols, DecimalFormat}

import anorm._
import play.api.db.DB
import play.api.Play.current

/**
 * Created by ubuntu on 04.11.15.
 */
class Db {

  def configurationOptions = DB.withConnection { implicit c =>
    SQL("""create or replace function build_product_variants(id_prod integer)returns
            table(is_configurable boolean,
            amount integer,
            id_hardware integer,
            id_var_hw integer,
            model_hardware text,
            name_hardware character varying(500),
            name_parameter character varying,
            value_parameter double precision,
            unit_measure character varying)
          AS
          $$
          begin
            RETURN QUERY
            select cp.is_configurable,
              cp.amount_elements,
              coalesce(cp.fk_id_hardware,hw_var.fk_id_hardware) as cp_id_hardware,
              coalesce(cp.fk_id_variant_hardware,hw_var.id_hardware_variant) as id_var_hw,
              hw_name.name_hardware||' '||hw_var_name.model as model_hardware,
              hw_name.name_hardware as name_hardware,
              par.name as name_parameter,
              vals.value as value_parameter,
              u_m.designation as unit_measure
            from composition_product as cp
              left join hardware as hw on(cp.fk_id_hardware=hw.id_hardware) left join hardware_variant as hw_var on(cp.fk_id_variant_hardware=hw_var.id_hardware_variant or hw.id_hardware=hw_var.fk_id_hardware)
              left join hardware as hw_name on(hw_name.id_hardware=cp.fk_id_hardware or hw_name.id_hardware=hw_var.fk_id_hardware)
              left join hardware_variant as hw_var_name on(hw_var_name.id_hardware_variant=cp.fk_id_variant_hardware or hw_var_name.id_hardware_variant=hw_var.id_hardware_variant)
              left join value_parameters as vals on(vals.fk_id_variant_hardware= hw_var_name.id_hardware_variant)
              left join parameter as par on(par.id_parameter=vals.fk_id_parameter)
              left join unit_measure as u_m on(u_m.id_unit_measure=par.fk_id_unit_measure)
            where cp.fk_id_product=id_prod
            order by is_configurable, cp_id_hardware;
          end;
          $$ LANGUAGE plpgsql;""")
  }
//
//  def collectProductParameters = DB.withConnection { implicit c =>
//    SQL("""create or replace function build_product_parameters(id_product integer)returns table (name_hardware character varying,name_parameter character varying,value_parameter character varying, unit_measure character varying) AS
//            $$
//          begin
//          return query
//          with recursive building_product(name_hardware,id_hardware,amount,price) as(
//          select hw.name_hardware, hw.id_hardware,pc.amount_elements,hw.price from product_composition pc left join hardware hw on (pc.fk_id_hardware=hw.id_hardware)
//          where pc.fk_id_product=id_product
//          union
//          select hw.name_hardware,hc.fk_id_hardware,hc.amount_elements,hw.price from building_product bp inner join composite_complex_set hc on(bp.id_hardware=hc.fk_id_complex_hardware)
//          left join hardware hw on(hc.fk_id_hardware=hw.id_hardware)
//          )
//          select bp.name_hardware,par.name_parameter,hp.value,um.designation from building_product bp inner join hardware_parameters hp on (bp.id_hardware=hp.fk_id_hardware)
//          left join parameters par on(hp.fk_id_parameter=par.id_parameter)
//          left join unit_measures um on(par.fk_unit_measure=um.id_unit_measure);
//          end;
//          $$ LANGUAGE plpgsql;""")
//  }


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
     SQL(s"select * from build_product_variants('$idProduct')")().map(row => (row[String](5),row[String](6),row[String](7),row[Double](8),row[String](9))).
       groupBy((tuple: (String, String, String, Double, String)) => tuple._2).
       map((tuple: (String, Stream[(String, String, String, Double, String)])) => tuple._1 -> tuple._2.size)
       .foreach(println)


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
