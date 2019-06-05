package cherish.component.batch.sync

import java.io._
import java.util.UUID

import cherish.component.batch.service.{FingerGradeService, JpaSaveOrUpdateService}
import cherish.component.config.BatchConfig
import cherish.component.jni.{NativeQualityScore, QualityImage}
import cherish.component.jpa.{QualityScore, QualityScoreRange, Tpcardimgmnt}
import cherish.component.util.{FtpUtil, KryoListConvertUtils}
import monad.core.services.{CronScheduleWithStartModel, StartAtDelay}
import monad.support.services.LoggerSupport
import org.apache.tapestry5.ioc.annotations.PostInjection
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor

import scala.collection.mutable

class FingerGradeSyncImpl(batchConfig : BatchConfig,
                          fingerGradeService: FingerGradeService,
                          jpaSaveOrUpdateService: JpaSaveOrUpdateService)extends LoggerSupport{


  var qualityScore :QualityScore = null
  /**
    * 捺印指纹质量评分定时任务
    * @param periodicExecutor
    */
  @PostInjection
  def startUp(periodicExecutor: PeriodicExecutor): Unit = {
    if(batchConfig.sync.syncCron != null){
      periodicExecutor.addJob(new CronScheduleWithStartModel(batchConfig.sync.syncCron , StartAtDelay), "sync-cron", new Runnable {
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
    val tpcardimgmntList = Tpcardimgmnt.selectByA("select t.* from tpcardimgmnt t,personinfo p where t.flag = 0 and t.personid = p.personid and p.person_level is not null and rownum <11")
    val tpcardimgmnts = tpcardimgmntList.iterator()
    logger.info("Tpcardimgmnt集合长度:" + tpcardimgmntList.size())
    System.loadLibrary("java_cherishqualityutil")
    //tpcardimgmnts 调用动态库打分，存入quality_score表
    while(tpcardimgmnts.hasNext){
      try {
        val tpcardimgmnt = tpcardimgmnts.next()
        val personId = tpcardimgmnt.personid
        logger.info(personId + "开始解析imgData")
        val imgDataList = KryoListConvertUtils.deserializationList(tpcardimgmnt.imgData)
        val imgDatas = imgDataList.iterator()
        logger.info(personId + "解析imgData 完成,imgDataList长度：" + imgDataList.size())
        qualityScore = new QualityScore(UUID.randomUUID().toString.replace("-", ""))
        var pScore: Double = 0  //平指采集独立得分
        var rScore: Double = 0  //滚指采集独立得分
        val ftpPath = batchConfig.ftpPath + "/" + personId
        while (imgDatas.hasNext) {
          val imageData = imgDatas.next()
          if (null != imageData.mntData) {
            try {
              logger.info(personId + "指位：" + imageData.fgp + "开始调用动态库打分")
              val fgp = if(imageData.fgp>10) imageData.fgp -10 else imageData.fgp
              val qualityImage = NativeQualityScore.GetQualityScore(imageData.imgData, imageData.mntData, fgp, "QualityImage") //图像打分
              logger.info(personId + "指位：" + imageData.fgp + "调用动态库打分结束:" + qualityImage.qualityScore)
              qualityScore = qualityScoreSet(imageData.fgp, qualityImage.qualityScore, qualityScore) //QualityScore 给打完分的指位赋值
              saveImg(personId, imageData.fgp, imageData.imgData, qualityImage.qualityImg) //保存原图和红白图
            } catch {
              case ex: Exception =>
                ex.printStackTrace()
            }
          } else {
            logger.info(personId + "指位：" + imageData.fgp + "没有特征，默认为0分")
            qualityScore = qualityScoreSet(imageData.fgp, 0, qualityScore) //没有特征默认0分
          }
        }

        logger.info("开始计算总分")
        pScore = ((qualityScore.rmp + qualityScore.rsp + qualityScore.rzp + qualityScore.lmp + qualityScore.lsp + qualityScore.lzp) * 3
          + (qualityScore.rhp + qualityScore.lhp) * 2
          + (qualityScore.rxp + qualityScore.lxp)) / 24
        logger.info("平指总分：" + pScore)
        rScore = ((qualityScore.rmr + qualityScore.rsr + qualityScore.rzr + qualityScore.lmr + qualityScore.lsr + qualityScore.lzr) * 3
          + (qualityScore.rhr + qualityScore.lhr) * 2
          + (qualityScore.rxr + qualityScore.lxr)) / 24
        logger.info("滚指总分：" + rScore)

        val totalScore = 0.75 * rScore + 0.25 * pScore
        logger.info("平均分：" + totalScore)

        val qualityScoreRangeList = QualityScoreRange.get.iterator()
        while (qualityScoreRangeList.hasNext) {
          val qualityScoreRange = qualityScoreRangeList.next()
          if(totalScore >= qualityScoreRange.minScore && totalScore <= qualityScoreRange.maxScore){
            qualityScore.qualityLevel = qualityScoreRange.level
            return
          }
        }

        qualityScore.cardid = personId
        qualityScore.imgUrl = ftpPath
        qualityScore.totalScore = totalScore
        jpaSaveOrUpdateService.qualityScoreSave(qualityScore)
        tpcardimgmnt.flag = 1
        jpaSaveOrUpdateService.tpcardimgmntUpdate(tpcardimgmnt)


        //根据设置分数判断人员指纹是否达标
        fingerGradeService.isQualified(personId)
      }catch {
        case e:Exception =>
          error("FingerGradeSyncImpl error:{}",e.getMessage,e)
          e.printStackTrace()
      }
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

    val saveOriginImg =  FtpUtil.uploadFile(batchConfig.ftpHost, batchConfig.ftpUserName, batchConfig.ftpPassword, batchConfig.ftpPort,  personId+ "/" , personId+ "_" + fgp +".bmp", new ByteArrayInputStream(origin))
    val saveRwImg =  FtpUtil.uploadFile(batchConfig.ftpHost, batchConfig.ftpUserName, batchConfig.ftpPassword, batchConfig.ftpPort,  personId + "/", personId+ "_" + fgp +".jpg", new ByteArrayInputStream(rwImg))
    if(!saveOriginImg) logger.info("保存原图-" + personId+ "_" + fgp +".bmp" + "- 失败")
    if(!saveRwImg) logger.info("保存红白图-" + personId+ "_" + fgp +".jpg" + "- 失败")
  }

}