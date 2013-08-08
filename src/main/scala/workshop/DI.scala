package solution

import scala.slick.driver.ExtendedProfile

object DI {

  object models {
    sealed trait Model { def id: Option[Long] }
    case class ModelA(a: String, id: Option[Long] = None) extends Model
    case class ModelB(b: String, id: Option[Long] = None) extends Model
  }

  trait Profile {
    val profile: ExtendedProfile
  }

  trait BaseTableComponent { this: Profile =>
    import profile.simple._
    
    abstract class BaseTable[T](name: String) extends Table[T](name) {
      def id: Column[Long]
      def forInsert = * returning id
    }
  }

  trait TableComponent { this: BaseTableComponent with Profile =>
    import profile.simple._
    import models._
    
    class ModelATable extends BaseTable[ModelA]("modelA") {
      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
      def a = column[String]("a")
      def * = a ~ id.? <> (ModelA.apply _, ModelA.unapply _)
    }

    class ModelBTable extends BaseTable[ModelB]("modelB") {
      def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
      def b = column[String]("b")
      def * = b ~ id.? <> (ModelB.apply _, ModelB.unapply _)
    }
  }

  trait CRUDComponent { this: BaseTableComponent with Profile =>
    import profile.simple._
    import models._  

    class TableCRUD[M <: Model, T[M] <: BaseTableComponent#BaseTable[M]](t: T[M]) {

      lazy val byId = t.createFinderBy(t => t.id)
      def insert(entity: M)(implicit session: Session): Long = {
        t.forInsert.insert(entity)
      }

      def insertAll1(entities: M*)(implicit session: Session): Seq[Long] = {
        insertAll(entities)(session)
      }

      def insertAll(entities: Seq[M])(implicit session: Session): Seq[Long] = {
        t.forInsert.insertAll(entities: _*)
      }

      def findAll(implicit session: Session): Seq[M] = q.list()

      def findById(id: Long)(implicit session: Session): Option[M] = byId(id).firstOption

      def q = Query(t)

      def update(id: Long, entity: M)(implicit session: Session) {
        q.where(_.id === id).update(entity)
      }

      def delete(id: Long)(implicit session: Session): Int = {
        val q1 = q.where(_.id === valueToConstColumn(id)).asInstanceOf[Query[Table[_], _]]
        queryToDeleteInvoker(q1).delete
      }

      def count(implicit session: Session): Int = Query(q.length).first
    }
  }

  class CRUD(val profile: ExtendedProfile)
    extends CRUDComponent
    with TableComponent with BaseTableComponent with Profile {

    val aTable = new ModelATable
    val bTable = new ModelBTable

    val modelACrud = new TableCRUD(aTable)
    val modelBCrud = new TableCRUD(bTable)
  }
}