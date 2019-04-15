package cherish.component.api.service

import org.springframework.transaction.annotation.Transactional

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/28
  */

trait PersonLevelSetService {
  @Transactional
  def personLevelSet(addressCode:String
                     ,raceCode:String
                     ,caseCode:String
                     ,gender:String
                     ,minAge:String
                     ,maxAge:String
                     ,criminalRecord:String): Unit

  def isSetPersonLevel:Int
}
