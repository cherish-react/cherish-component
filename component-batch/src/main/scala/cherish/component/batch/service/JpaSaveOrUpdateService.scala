package cherish.component.batch.service

import cherish.component.jpa.{PersonInfo, QualityScore, Tpcardimgmnt, WorkQueue}
import org.springframework.transaction.annotation.Transactional

/**
  * @author mengxin
  * @since 2019/05/24
  */
trait JpaSaveOrUpdateService {

    @Transactional
    def updatePersonInfo(personInfo: PersonInfo, personLevel:Int, levelId:String): Unit
    @Transactional
    def updatePersonInfo(personInfo: PersonInfo): Unit

    @Transactional
    def workQueueSave(workQueue: WorkQueue):Unit

    @Transactional
    def workQueueUpdate(workQueue: WorkQueue):Unit

    @Transactional
    def qualityScoreSave(qualityScore: QualityScore):Unit

    @Transactional
    def tpcardimgmntUpdate(tpcardimgmnt: Tpcardimgmnt):Unit

}
