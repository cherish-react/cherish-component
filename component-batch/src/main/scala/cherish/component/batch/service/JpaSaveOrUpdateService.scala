package cherish.component.batch.service

import cherish.component.jpa.{PersonInfo, QualityScore, WorkQueue}
import org.springframework.transaction.annotation.Transactional

/**
  * @author mengxin
  * @since 2019/05/24
  */
trait JpaSaveOrUpdateService {

    @Transactional
    def updatePersonInfo(personInfo: PersonInfo, personLevel:Int, levelId:String): Unit

    @Transactional
    def workQueueSave(workQueue: WorkQueue):Unit

    @Transactional
    def workQueueUpdate(workQueue: WorkQueue):Unit

    @Transactional
    def qualityScoreSave(qualityScore: QualityScore):Unit

}
