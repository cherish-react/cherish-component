package cherish


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
      Class.forName("cherish.component.BatchModule"),
      Class.forName("cherish.component.DataSourceModule"),
      Class.forName("cherish.component.ActiveRecordModule")
    )
    val loggger = LoggerFactory.getLogger(classOf[App])
    val webConfig = new WebServerConfig
    webConfig.bind = "127.0.0.1:8080"
    startServer(webConfig,"app",classes:_*)
    loggger.info("started")
    join
  }
}
