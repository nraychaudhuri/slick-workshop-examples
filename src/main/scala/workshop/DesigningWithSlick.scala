
package workshop

import scala.slick.driver.H2Driver.simple._
import utils.ConnectionPool._

object SessionTypes extends Enumeration {
  type SessionType = Value
  val LightingTalk, OneHourTalk, Tutorial = Value

  private val ser: SessionType => Int = s => s.id
  private val deser: Int => SessionType = i => SessionTypes.apply(i)

  implicit val sessionTypeMapper = MappedTypeMapper.base[SessionType, Int](ser, deser)

}

object DesigningWithSlick {

  import SessionTypes._

  case class Submission(description: String, sessionType: SessionType, name: String, bio: String, twitterId: Option[String])
  class ProporsalUsecases {
    import allTables._


    def submitTalk(s: Submission) = {
      //apply any biz logic on submission
      implicit val session = createSession()
      session.withTransaction {        
        val speakerId = speakers.forInsert.insert(Speaker(s.name, s.bio, s.twitterId))
        talks.forInsert.insert(Talk(s.description, s.sessionType, speakerId))
      }
    }
  }

  object allTables {
    val talks = new Talks
    val speakers = new Speakers
  }

  case class Talk(description: String, sessionType: SessionType, speakerId: Long, id: Option[Long] = None)
  case class Speaker(name: String, bio: String, twitterId: Option[String], id: Option[Long] = None)

  class Talks extends Table[Talk]("TALKS") {
    //all the columns needs to be defined as def due to cloning
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def description = column[String]("description")
    def sessionType = column[SessionType]("sessionType")
    def speakerId = column[Long]("speakerId")
    def columns = description ~ sessionType ~ speakerId

    def speaker1 = foreignKey("SPK_FK", speakerId, new Speakers)(_.id)

    def forInsert = columns <> ({ t => Talk(t._1, t._2, t._3) },
      { (u: Talk) => Some((u.description, u.sessionType, u.speakerId)) })

    def * = columns ~ id.? <> (Talk.apply _, Talk.unapply _)
  }

  class Speakers extends Table[Speaker]("Speaker") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def bio = column[String]("bio")
    def twitterId = column[Option[String]]("twitterId")

    def columns = name ~ bio ~ twitterId
    def forInsert = columns <> (s => Speaker(s._1, s._2, s._3), (s: Speaker) => Some((s.name, s.bio, s.twitterId)))

    def * = columns ~ id.? <> (Speaker.apply _, Speaker.unapply _)
  }
}