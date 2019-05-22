package cherish.component

import cherish.component.batch.internal.{BatchIsQualifiedServiceImpl, ExtractDataServiceImpl, FingerGradeServiceImpl}
import cherish.component.batch.service.{BatchIsQualifiedService, ExtractDataService, FingerGradeService}
import org.apache.tapestry5.ioc.ServiceBinder

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/9
  */
object BatchModule {


  def bind(binder: ServiceBinder): Unit ={
    binder.bind(classOf[ExtractDataService],classOf[ExtractDataServiceImpl])
    binder.bind(classOf[FingerGradeService],classOf[FingerGradeServiceImpl])
    binder.bind(classOf[BatchIsQualifiedService],classOf[BatchIsQualifiedServiceImpl])
  }

}
