package workshop

import scala.slick.driver.H2Driver.simple._
import java.sql.Date
import scala.slick.lifted.SimpleFunction
object UsingDBFunctions {

  class DBFunction extends Table[(Long, Date)]("dbfunction_tbl") {
    def id = column[Long]("id", O.PrimaryKey)
    def date = column[Date]("date")
    
    def * = id ~ date
  }
  
  def dayOfTheYear(c: Column[Date]) = SimpleFunction.apply("DAY_OF_YEAR")(TypeMapper.IntTypeMapper)(Seq(c))  

  //REPL session
// scala>   val tbl = new DBFunction
// tbl: workshop.UsingDBFunctions.DBFunction = Table dbfunction_tbl

// scala>   //this will generate the following query

// scala>   Query(tbl).map(t => dayOfTheYear(t.date)).selectStatement 
// res1: String = select DAY_OF_YEAR(x2."date") from "dbfunction_tbl" x2
}