package cherish


import com.react.config.WebServerConfig
import com.react.server.JettyServerSupport
import monad.core.MonadCoreSymbols
import monad.support.services.LoggerSupport
import org.slf4j.LoggerFactory

/**
 * Hello world!
 *
 */
object App extends JettyServerSupport with LoggerSupport{
  def main(args: Array[String]): Unit = {
    val serverHome = System.getProperty(MonadCoreSymbols.SERVER_HOME, "support")
    System.setProperty(MonadCoreSymbols.SERVER_HOME, serverHome)

    val classes = List[Class[_]](
      Class.forName("cherish.component.api.DataSourceModule"),
      Class.forName("cherish.component.BatchModule"),
      Class.forName("cherish.component.BatchCronModule"),
      Class.forName("cherish.component.HallBatchModule"),
      Class.forName("cherish.component.api.LocalApiConfigurationModule"),
      Class.forName("cherish.component.database.jpa.EntityManagerSourceModule")
    )
    val loggger = LoggerFactory.getLogger(classOf[App])
    val webConfig = new WebServerConfig
    webConfig.bind = "0.0.0.0:10000"
    startServer(webConfig,"cherish.component",classes:_*)
    loggger.info("started")
    join
  }
}
