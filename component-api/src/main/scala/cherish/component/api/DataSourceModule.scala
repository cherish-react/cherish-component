package cherish.component.api

import java.sql.Connection
import javax.sql.DataSource

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import net.sf.log4jdbc.ConnectionSpy
import org.apache.tapestry5.ioc.Configuration
import org.apache.tapestry5.ioc.annotations.EagerLoad
import org.apache.tapestry5.ioc.services.RegistryShutdownHub

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/9
  */
object DataSourceModule {

  @EagerLoad
  def buildDataSource(hub: RegistryShutdownHub): DataSource ={
    val hikariConfig = new HikariConfig()
    hikariConfig.setDriverClassName("oracle.jdbc.driver.OracleDriver")
    hikariConfig.setJdbcUrl("jdbc:oracle:thin:@192.168.1.214:1521:oragafis")
    hikariConfig.setUsername("afis")
    hikariConfig.setPassword("helloafis")
    hikariConfig.setConnectionTestQuery("select 1 from dual")
    hikariConfig.setAutoCommit(false)
    hikariConfig.addDataSourceProperty("cachePrepStmts", "true")
    hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250")
    hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048")
    hikariConfig.setMaximumPoolSize(5)
    val dataSource = new HikariDataSource(hikariConfig){
      override def getConnection: Connection = {
        new ConnectionSpy(super.getConnection)
      }
    }
    hub.addRegistryShutdownListener(new Runnable {
      override def run() = {
        dataSource.close
      }
    })
    dataSource
  }

  def contributeEntityManagerFactory(configuration:Configuration[String]): Unit ={
    configuration.add("cherish.component.api.jpa")
  }
}
