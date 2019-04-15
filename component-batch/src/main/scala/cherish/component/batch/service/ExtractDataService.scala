package cherish.component.batch.service

import cherish.component.jpa.Information
import org.springframework.transaction.annotation.Transactional

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/9
  */
trait ExtractDataService {

  @Transactional
  def saveData(information: Information):Information

  @Transactional
  def updateData(information: Information):Information
}
