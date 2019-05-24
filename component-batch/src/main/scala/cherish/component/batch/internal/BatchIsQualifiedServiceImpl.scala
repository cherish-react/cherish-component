package cherish.component.batch.internal

import java.util.{Date, UUID}
import java.util.concurrent.Executors

import cherish.component.batch.service.{BatchIsQualifiedService, FingerGradeService, JpaSaveOrUpdateService}
import cherish.component.config.HallBatchConfig
import cherish.component.jpa.{PersonInfo, WorkQueue}
import cherish.component.util.HttpClientUtils
import monad.support.services.LoggerSupport

/**
  * 线程处理人员原始数据是否达标
  * @author mengxin
  * @since 2019/5/17
  */
class BatchIsQualifiedServiceImpl(fingerGradeService: FingerGradeService,
                                  hallBatchConfig: HallBatchConfig,
                                  jpaSaveOrUpdateService: JpaSaveOrUpdateService) extends BatchIsQualifiedService with LoggerSupport{

  private lazy val executor = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())

  override def batchIsQualified(): Unit ={
    val personIdList = PersonInfo.select("select personid from personinfo where person_level is not null").getResultList.asInstanceOf[java.util.List[String]]
    val personids = personIdList.iterator()

    if(personIdList.size()>0){
      val workQueue = new WorkQueue()
      workQueue.id = UUID.randomUUID().toString.replace("-","")
      workQueue.insertTime = new Date()
      workQueue.beginTime = new Date()
      workQueue.workType = 2
      workQueue.workState = 1
      jpaSaveOrUpdateService.workQueueSave(workQueue)

      executor.execute(new Runnable {
        override def run(): Unit ={
          while(personids.hasNext){
            val personid = personids.next()
//            executor.execute(new Runnable {
//              override def run(): Unit = {
                try{
                  info("开始判断是否达标：" + personid)
                  fingerGradeService.isQualified(personid)

                }catch{
                  case ex:Exception =>
                    error("batchFingerGrade failed: personid{},message{}",personid,ex.getStackTrace.toString)
                    ex.printStackTrace()
                }
//              }
//            })
          }
          //判断是否达标完成后，修改work_queue状态,并通知统计服务
          workQueue.endTime = new Date()
          workQueue.workState = 2
          jpaSaveOrUpdateService.workQueueUpdate(workQueue)
          try{
            val result = HttpClientUtils.doGet(hallBatchConfig.rpc + "/pages/statistics/changeWorkQueueState/2")
          }catch{
            case ex:Exception =>
              error("httpClient错误："+ex.getMessage)
              ex.printStackTrace()
          }
        }
      })
    }

  }
}
