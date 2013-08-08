
package workshop

import scala.slick.driver.BasicDriver.simple.Table

object ModelHierarchyToTable extends App {

  trait Base {
    val a: Int
    val b: String
  } // it can be in some cases abstract, and in some cases not
  case class ChildOne(val a: Int, val b: String, val c: String) extends Base
  case class ChildTwo(val a: Int, val b: String, val d: Int) extends Base

  class MyTable extends Table[Base]("SOME_TABLE") {
    def a = column[Int]("a")
    def b = column[String]("b")
    def c = column[String]("c", O.Nullable)
    def d = column[Int]("d", O.Nullable)
    def e = column[String]("e")

    def * = a ~ b ~ c.? ~ d.? ~ e <> ({ t => t match {
      case (a, b, Some(c), _, "ChildOne") => ChildOne(a, b, c)
      case (a, b, _, Some(d), "ChildTwo") => ChildTwo(a, b, d)
      case _ => error("oops")
    }}, {
      case ChildOne(a, b, c) => Some((a, b, Some(c), None, "ChildOne"))
      case ChildTwo(a, b, d) => Some((a, b, None, Some(d), "ChildTwo"))
      case _ => error("oops")
    })
  }
}
  