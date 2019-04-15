package cherish.component.batch.internal


import cherish.component.batch.service.ExtractDataService
import cherish.component.jpa.Information

import scala.collection.mutable.ArrayBuffer

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/9
  */
class ExtractDataServiceImpl extends ExtractDataService{

  override def saveData(information: Information): Information = information.save

  override def updateData(information: Information): Information = information.update
}
