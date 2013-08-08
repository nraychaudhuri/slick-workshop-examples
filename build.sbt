name := "slick-examples"

organization := "Typesafe Inc."

scalaVersion := "2.10.2"

libraryDependencies ++= List(
  // use the right Slick version here:
  "com.typesafe.slick" %% "slick" % "1.0.1",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.h2database" % "h2" % "1.3.166",
  "org.xerial" % "sqlite-jdbc" % "3.7.2",
  "org.scalatest" %% "scalatest" % "2.0.M6-SNAP26" % "test",
  "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"
)

initialCommands in console := """
	import scala.slick.driver.H2Driver.simple._
	import utils.ConnectionPool._
	import workshop._
	import scala.slick.lifted.{Query => q}
	import scala.slick.driver.H2Driver
	import scala.slick.driver.SQLiteDriver
	val session = createSession()
    """
    
cleanupCommands in console := """
      println("Closing the session")
      session.close() 
    """    