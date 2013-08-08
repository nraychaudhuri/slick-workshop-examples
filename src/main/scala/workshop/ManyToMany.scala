
package workshop

import scala.slick.driver.H2Driver.simple._
import utils.ConnectionPool._

object ManyToMany {

  val a = new A
  val b = new B
  val ab = new AToB
  class A extends Table[(Int, String)]("a") {
    def id = column[Int]("id", O.PrimaryKey)
    def s = column[String]("s")
    def * = id ~ s
    def bs = ab.filter(_.aId === id).flatMap(_.bFK)
  }

  class B extends Table[(Int, String)]("b") {
    def id = column[Int]("id", O.PrimaryKey)
    def s = column[String]("s")
    def * = id ~ s
    def as = ab.filter(_.bId === id).flatMap(_.aFK)
  }

  class AToB extends Table[(Int, Int)]("a_to_b") {
    def aId = column[Int]("a")
    def bId = column[Int]("b")
    def * = aId ~ bId
    def aFK = foreignKey("a_fk", aId, a)(a => a.id)
    def bFK = foreignKey("b_fk", bId, b)(b => b.id)
  }

  //val session = createSession()

//  (a.ddl ++ b.ddl ++ ab.ddl).create(session)
//  a.insertAll(1 -> "a", 2 -> "b", 3 -> "c")(session)
//  b.insertAll(1 -> "x", 2 -> "y", 3 -> "z")(session)
//  ab.insertAll(1 -> 1, 1 -> 2, 2 -> 2, 2 -> 3)(session)
  //this works
  val q1 = for {
	 a <- Query(a) if a.id >= 2
	 aToB <- Query(ab) if aToB.aId === a.id
     b <- Query(b) if b.id === aToB.bId
  } yield (a.s, b.s)
  
//this fails due to a bug which is fixed in slick 2  
//  val q1 = for {
//    a1 <- Query(a) if a.id >= 2
//    b <- a1.bs
//  } yield (a.s, b.s)
//  q1.foreach(x => println(" " + x))(session)
//  
  
  
}