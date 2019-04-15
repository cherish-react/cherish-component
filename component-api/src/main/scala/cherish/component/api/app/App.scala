package cherish.component.api.app

import com.react.config.WebServerConfig
import com.react.server.JettyServerSupport
import monad.support.services.LoggerSupport
import org.slf4j.LoggerFactory

/**
 * Hello world!
 *
 */
object App extends JettyServerSupport with LoggerSupport{
  def main(args: Array[String]): Unit = {
    val classes = List[Class[_]](
      Class.forName("cherish.component.api.LocalApiServiceModule"),
      Class.forName("cherish.component.api.LocalApiConfigurationModule"),
      Class.forName("cherish.component.api.DataSourceModule"),
      Class.forName("cherish.component.database.jpa.EntityManagerSourceModule")
    )
    val logger = LoggerFactory.getLogger(classOf[App])
    val webConfig = new WebServerConfig
    webConfig.bind = "127.0.0.1:8081"
    startServer(webConfig,"cherish.component.api",classes:_*)
    logger.info("started")
    join
  }
}
