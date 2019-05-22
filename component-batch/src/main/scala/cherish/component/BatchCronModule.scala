package cherish.component

import nirvana.hall.api.internal.sync.FingerGradeSyncImpl
import org.apache.tapestry5.ioc.ServiceBinder


/**
  * @author mengxin
  * @since 2019/5/15
  */
object BatchCronModule {


  def bind(binder: ServiceBinder): Unit ={
//    binder.bind(classOf[FingerGradeSyncImpl]).eagerLoad()
  }

}
