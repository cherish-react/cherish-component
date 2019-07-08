package cherish.component.batch.internal

import cherish.component.batch.service.JpaSaveOrUpdateService
import cherish.component.jpa.{PersonInfo, QualityScore, Tpcardimgmnt, WorkQueue}
import monad.support.services.LoggerSupport


/**
  * @author mengxin
  * @since 2019/5/24
  */
class JpaSaveOrUpdateServiceImpl extends JpaSaveOrUpdateService with LoggerSupport{


  def updatePersonInfo(personInfo: PersonInfo, personLevel:Int, levelId:String): Unit ={
    personInfo.personLevel = personLevel
    personInfo.levelId = levelId
    personInfo.update()
    logger.info("人员定级成功:"+ personInfo.personid)
  }

  def updatePersonInfo(personInfo: PersonInfo): Unit ={
    personInfo.update()
  }

  override def workQueueSave(workQueue: WorkQueue): Unit ={
    workQueue.save()
  }

  override def workQueueUpdate(workQueue: WorkQueue): Unit ={
    workQueue.update()
  }

  override def qualityScoreSave(qualityScore: QualityScore): Unit ={
    qualityScore.save()
  }

  override def qualityScoreUpdate(qualityScore: QualityScore): Unit ={
    qualityScore.update()
  }

  override def tpcardimgmntUpdate(tpcardimgmnt: Tpcardimgmnt): Unit ={
    tpcardimgmnt.update()
  }

}
