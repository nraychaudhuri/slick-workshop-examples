
package utils

import scala.slick.session.Database
import org.h2.jdbcx.JdbcDataSource
import scala.slick.session.Session
import scala.slick.session.BaseSession

object ConnectionPool {

  def createSession() = {
    import org.h2.jdbcx.JdbcDataSource;
    val ds = new JdbcDataSource();
    ds.setURL("jdbc:h2:mem:test");
    ds.setUser("sa");
    ds.setPassword("sa");
    Database.forDataSource(ds).createSession
  }
  
  def inMemorySession() = {
    
  }
}