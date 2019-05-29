package cherish.component

import cherish.component.config.{BatchConfig}
import monad.core.MonadCoreSymbols
import monad.core.internal.MonadConfigFileUtils
import monad.support.services.XmlLoader
import org.apache.tapestry5.ioc.annotations.Symbol

/**
 * @author mengxin
 * @since 2019-5-15
 */
object BatchConfigModule {
  def buildBatchConfig(@Symbol(MonadCoreSymbols.SERVER_HOME) serverHome: String) = {
    val content = MonadConfigFileUtils.readConfigFileContent(serverHome, "cherish-batch.xml")
    XmlLoader.parseXML[BatchConfig](content)
  }
}
