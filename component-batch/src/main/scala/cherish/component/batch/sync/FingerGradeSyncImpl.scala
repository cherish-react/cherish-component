package cherish.component.batch.sync

import java.io._
import java.util.UUID

import cherish.component.batch.service.{FingerGradeService, JpaSaveOrUpdateService}
import cherish.component.config.HallBatchConfig
import cherish.component.jni.{NativeQualityScore, QualityImage}
import cherish.component.jpa.{QualityScore, Tpcardimgmnt}
import cherish.component.util.{FtpUtil, KryoListConvertUtils}
import monad.core.services.{CronScheduleWithStartModel, StartAtDelay}
import monad.support.services.LoggerSupport
import org.apache.tapestry5.ioc.annotations.PostInjection
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor

class FingerGradeSyncImpl(hallBatchConfig : HallBatchConfig,
                          fingerGradeService: FingerGradeService,
                          jpaSaveOrUpdateService: JpaSaveOrUpdateService)extends LoggerSupport{


  var qualityScore :QualityScore = null
  /**
    * 捺印指纹质量评分定时任务
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
              error("FingerGradeSyncImpl error:{}",e.getMessage,e)
          }
          info("end sync-cron")
        }
      })
    }
  }

  def doWork(): Unit ={
    val tpcardimgmntList = Tpcardimgmnt.selectByA("select t.* from tpcardimgmnt t,personinfo p where t.flag = 0 and t.personid = p.personid and p.person_level is not null and rownum <11").iterator()
    System.loadLibrary("java_cherishqualityutil")

    //遍历personinfoList 调用动态库打分，存入quality_score表
    while(tpcardimgmntList.hasNext){
      val tpcardimgmnt = tpcardimgmntList.next()
      val imgDataList = KryoListConvertUtils.deserializationList(tpcardimgmnt.imgData).iterator()
      qualityScore = new QualityScore(UUID.randomUUID().toString.replace("-",""))
      var pScore :Double = 0   //平指采集独立得分
      var rScore :Double = 0   //滚指采集独立得分
      val personId = tpcardimgmnt.personid
      val ftpPath = hallBatchConfig.ftpPath + "/" + personId
      while(imgDataList.hasNext){
        val imageData = imgDataList.next()
        if(null != imageData.mntData){
          try {
            val qualityImage = NativeQualityScore.GetQualityScore(imageData.imgData, imageData.mntData, imageData.fgp, "QualityImage") //图像打分
            qualityScore = qualityScoreSet(imageData.fgp, qualityImage.qualityScore*100, qualityScore) //QualityScore 给打完分的指位赋值
            saveImg(personId, imageData.fgp, imageData.imgData, qualityImage.qualityImg) //保存原图和红白图
          }catch {
            case ex:Exception =>
              ex.printStackTrace()
          }
        }else{
          qualityScore = qualityScoreSet(imageData.fgp, 0, qualityScore) //没有特征默认0分
        }
      }

      pScore = ((qualityScore.rmp + qualityScore.rsp + qualityScore.rzp + qualityScore.lmp + qualityScore.lsp + qualityScore.lzp)*3
          +(qualityScore.rhp + qualityScore.lhp)*2
          +(qualityScore.rxp + qualityScore.lxp)) /24

      rScore = ((qualityScore.rmr + qualityScore.rsr + qualityScore.rzr + qualityScore.lmr + qualityScore.lsr + qualityScore.lzr)*3
        +(qualityScore.rhr + qualityScore.lhr)*2
        +(qualityScore.rxr + qualityScore.lxr)) /24

      val totalScore = 0.75 * rScore + 0.25 * pScore

      qualityScore.cardid = personId
      qualityScore.imgUrl = ftpPath
      qualityScore.totalScore = totalScore
      jpaSaveOrUpdateService.qualityScoreSave(qualityScore)
      tpcardimgmnt.flag = 1
      jpaSaveOrUpdateService.tpcardimgmntUpdate(tpcardimgmnt)


      //根据设置分数判断人员指纹是否达标
      fingerGradeService.isQualified(personId)
    }

  }

  def qualityScoreSet(fgp :Int,imageScore: Float,qualityScore: QualityScore ): QualityScore ={

    fgp match {
      case 1 =>
        qualityScore.rmr = imageScore
      case 2 =>
        qualityScore.rsr = imageScore
      case 3 =>
        qualityScore.rzr = imageScore
      case 4 =>
        qualityScore.rhr = imageScore
      case 5 =>
        qualityScore.rxr = imageScore
      case 6 =>
        qualityScore.lmr = imageScore
      case 7 =>
        qualityScore.lsr = imageScore
      case 8 =>
        qualityScore.lzr = imageScore
      case 9 =>
        qualityScore.lhr = imageScore
      case 10 =>
        qualityScore.lxr = imageScore
      case 11 =>
        qualityScore.rmp = imageScore
      case 12 =>
        qualityScore.rsp = imageScore
      case 13 =>
        qualityScore.rzp = imageScore
      case 14 =>
        qualityScore.rhp = imageScore
      case 15 =>
        qualityScore.rxp = imageScore
      case 16 =>
        qualityScore.lmp = imageScore
      case 17 =>
        qualityScore.lsp = imageScore
      case 18 =>
        qualityScore.lzp = imageScore
      case 19 =>
        qualityScore.lhp = imageScore
      case 20 =>
        qualityScore.lxp = imageScore
    }
    qualityScore
  }

  /**
    * 保存指纹原图和红白图
    */
  def saveImg(personId :String, fgp: Int, origin : Array[Byte], rwImg : Array[Byte]): Unit ={

    val saveOriginImg =  FtpUtil.uploadFile(hallBatchConfig.ftpHost, hallBatchConfig.ftpUserName, hallBatchConfig.ftpPassword, hallBatchConfig.ftpPort,  personId+ "/" , personId+ "_" + fgp +".bmp", new ByteArrayInputStream(origin))
    val saveRwImg =  FtpUtil.uploadFile(hallBatchConfig.ftpHost, hallBatchConfig.ftpUserName, hallBatchConfig.ftpPassword, hallBatchConfig.ftpPort,  personId + "/", personId+ "_" + fgp +".jpg", new ByteArrayInputStream(rwImg))
    if(!saveOriginImg) logger.info("保存原图-" + personId+ "_" + fgp +".bmp" + "- 失败")
    if(!saveRwImg) logger.info("保存红白图-" + personId+ "_" + fgp +".jpg" + "- 失败")
  }

}