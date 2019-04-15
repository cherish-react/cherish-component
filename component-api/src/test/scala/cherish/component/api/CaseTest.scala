package cherish.component.api

import org.apache.tapestry5.ioc.{Registry, RegistryBuilder}
import org.junit.Before

import scala.reflect.{ClassTag, classTag}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/11
  */
class CaseTest {
  private var registry:Registry = _
  protected def getService[T:ClassTag]:T={
    registry.getService(classTag[T].runtimeClass.asInstanceOf[Class[T]])
  }
  @Before
  def setup: Unit ={
    val modules = Seq[String](
      "cherish.component.api.LocalApiServiceModule",
      "cherish.component.api.LocalApiConfigurationModule",
      "cherish.component.api.DataSourceModule",
      "cherish.component.database.jpa.EntityManagerSourceModule"
    ).map(Class.forName)
    registry = RegistryBuilder.buildAndStartupRegistry(modules: _*)
  }
}
