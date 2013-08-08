package workshop

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.BaseTypeMapper

object GenericFindByExample {

	def findBy[T, S](tbl: Table[S], c: Column[T], v: T)(implicit baseMapper: BaseTypeMapper[T]) = {
      for {
        t <- tbl where (t => c === ConstColumn(v))
      } yield t.*
    }
}