package workshop

import org.scalatest.{ Matchers, WordSpec }
import utils.ConnectionPool
import org.scalatest.BeforeAndAfter
import DI._
import scala.slick.driver.H2Driver

class CRUDSpec extends WordSpec with Matchers with BeforeAndAfter {
  
  val session = ConnectionPool.createSession()
  
  val h2Crud = new CRUD(H2Driver)
  
  import h2Crud._
  import h2Crud.profile.simple._
  import models._
  
  def createTables(): Unit = {
    aTable.ddl.create(session)  
  }

  before {
    createTables()
  }
  
  after {
    session.close()
  }
  
  "Model a" should {
    "save and retrieve model a" in {
      modelACrud.insert(ModelA("some model a"))(session) should be(1) 
      
      modelACrud.findAll(session) should be(Seq(ModelA("some model a", Some(1))))
    }
  }
  
  
  
  

//  "Creating a Time" should {
//    "throw an IllegalArgumentException for hours not within 0 and 23" in {
//      evaluating(Time(-1)) should produce[IAE]
//      evaluating(Time(24)) should produce[IAE]
//    }
//    "throw an IllegalArgumentException for minutes not within 0 and 59" in {
//      evaluating(Time(minutes = -1)) should produce[IAE]
//      evaluating(Time(minutes = 60)) should produce[IAE]
//    }
//  }
//
//  "The default arguments for hours and minutes" should {
//    "be equal to 0" in {
//      val time = Time()
//      time.hours shouldEqual 0
//      time.minutes shouldEqual 0
//    }
//  }
//
//  "asMinutes" should {
//    "be initialized correctly" in {
//      Time(1, 40).asMinutes shouldEqual 100
//    }
//  }
//
//  "Calling minus or -" should {
//    "return the correct difference in minutes" in {
//      Time(1, 40) minus Time(1, 10) shouldEqual 30
//      Time(1, 40) - Time(1, 10) shouldEqual 30
//    }
//  }
}
