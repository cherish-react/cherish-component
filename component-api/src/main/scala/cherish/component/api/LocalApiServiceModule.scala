package cherish.component.api

import cherish.component.api.internal.{PersonLevelSetServiceImpl, QualityScoreRangeSetServiceImpl}
import cherish.component.api.service.{PersonLevelSetService, QualityScoreRangeSetService}
import org.apache.tapestry5.ioc.ServiceBinder

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */
object LocalApiServiceModule {
  def bind(binder: ServiceBinder): Unit ={
    binder.bind(classOf[PersonLevelSetService],classOf[PersonLevelSetServiceImpl])
    binder.bind(classOf[QualityScoreRangeSetService],classOf[QualityScoreRangeSetServiceImpl])
  }
}
