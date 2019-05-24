package cherish.component.batch.sync

import java.util.Date

import cherish.component.api.jpa.{CaseDimen, HukouDimen, PersonLevel}
import cherish.component.batch.service.BatchIsQualifiedService
import cherish.component.config.HallBatchConfig
import cherish.component.jpa.{PersonInfo, QualityScore, WorkQueue}
import monad.core.services.{CronScheduleWithStartModel, StartAtDelay}
import monad.support.services.LoggerSupport
import org.apache.tapestry5.ioc.annotations.PostInjection
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor

class PersonGradeSyncImpl(hallBatchConfig : HallBatchConfig,
                          batchIsQualifiedService: BatchIsQualifiedService)extends LoggerSupport{

  /**
    * 人员定级定时任务
    * @param periodicExecutor
    */
  @PostInjection
  def startUp(periodicExecutor: PeriodicExecutor): Unit = {
    if(hallBatchConfig.sync.syncCron != null){
      periodicExecutor.addJob(new CronScheduleWithStartModel(hallBatchConfig.sync.syncCron , StartAtDelay), "sync-cron", new Runnable {
        override def run(): Unit = {
          info("begin sync-cron")
          try{
            doWork
          }catch{
            case e:Exception =>
              error("PersonGradeSyncImpl error:{}",e.getMessage,e)
          }
          info("end sync-cron")
        }
      })
    }
  }

  def doWork(): Unit ={
    //查询personinfo表中person_level和 level_id为空的数据
    val personInfoList = PersonInfo.selectByA("select * from personinfo where person_level is null and rownum <11")
    if(personInfoList.size()>0) {
      val personLevelList = PersonLevel.findBy_flag(1)
      val personLevel = if(personLevelList.size()>0) personLevelList.get(0) else null
      // 人员定级标准表 flag=1
      val hukouDimen = HukouDimen.select("select address_code from hukou_dimen where dimen_id = '" + personLevel.hukouDimenId +"'").getResultList.asInstanceOf[java.util.ArrayList[String]]
      val caseDimen = CaseDimen.select("select case_code from case_dimen where dimen_id =' " + personLevel.caseDimenId +"'").getResultList.asInstanceOf[java.util.ArrayList[String]]

      for (i <- 0 to personInfoList.size()) {
        val personInfo = personInfoList.get(i)
        if (personLevel == null) {
          updatePersonInfo(personInfo, 3, personLevel.id)
        } else {
          val birthAddressCode = personInfo.birthAddressCode
          //人员出生地代码
          val criminalRecord = personInfo.criminalRecord
          //前科标识
          val caseType1 = personInfo.caseType1
          //案件类别1
          val caseType2 = personInfo.caseType2
          val caseType3 = personInfo.caseType3

          val is_hukou_dimen: Boolean = hukouDimen.contains(birthAddressCode)
          val is_case_dimen: Boolean = caseDimen.contains(caseType1) || caseDimen.contains(caseType2) || caseDimen.contains(caseType3)

          if (is_hukou_dimen) {
            if (is_case_dimen) {
              //满足两种定级标准为A级
              updatePersonInfo(personInfo, 1, personLevel.id)
            } else {
              //满足一种定级标准为B级
              updatePersonInfo(personInfo, 2, personLevel.id)
            }
          } else {
            if (is_case_dimen) {
              //满足一种定级标准为B级
              updatePersonInfo(personInfo, 2, personLevel.id)
            } else {
              //两种定级标准均不满足为C级
              updatePersonInfo(personInfo, 3, personLevel.id)
            }
          }
        }
      }
    }else{
      //如果所有人员定级结束并且work_queue表存在work_type=1 and work_state=1 的数据，修改work_state为2并调用人员达标判断
      val workQueueList = WorkQueue.findBy_workType_and_workState(1,1)    //理论最多只存在一条数据
      if(workQueueList.size()>0){
        var workQueue = workQueueList.get(0)
        workQueue.workState = 2
        workQueue.endTime = new Date()
        workQueue.update()

        batchIsQualifiedService.batchIsQualified()
      }
    }

  }

  def updatePersonInfo(personInfo: PersonInfo, personLevel:Int, levelId:String): Unit ={
    personInfo.personLevel = personLevel
    personInfo.levelId = levelId
    personInfo.update()
    logger.info("人员定级成功:"+ personInfo.personid)
  }

}