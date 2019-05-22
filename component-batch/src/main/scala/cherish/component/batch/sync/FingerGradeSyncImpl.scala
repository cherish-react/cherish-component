package nirvana.hall.api.internal.sync

import java.io._

import cherish.component.batch.service.FingerGradeService
import cherish.component.config.HallBatchConfig
import cherish.component.jni.{NativeQualityScore, QualityImage}
import cherish.component.jpa.{ImageData, PersonInfo, QualityScore, Tpcardimgmnt}
import cherish.component.util.{FtpUtil, KryoListConvertUtils}
import monad.core.services.{CronScheduleWithStartModel, StartAtDelay}
import monad.support.services.LoggerSupport
import org.apache.tapestry5.ioc.annotations.PostInjection
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor

class FingerGradeSyncImpl(hallBatchConfig : HallBatchConfig,
                          fingerGradeService: FingerGradeService)extends LoggerSupport{

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
    val tpcardimgmntList = Tpcardimgmnt.select("select t.* from tpcardimgmnt t,personinfo p where t.flag = 0 and t.personid = p.personid and p.personLevel is not null and rownum <11").getResultList.asInstanceOf[java.util.ArrayList[Tpcardimgmnt]].iterator()
    System.loadLibrary("java_cherishqualityutil")

    //遍历personinfoList 调用动态库打分，存入quality_score表
    while(tpcardimgmntList.hasNext){
      val tpcardimgmnt = tpcardimgmntList.next()
      val imgDataList = KryoListConvertUtils.deserializationList(tpcardimgmnt.imgData).iterator()
      var qualityScore = new QualityScore
      var pScore :Double = 0   //平指采集独立得分
      var rScore :Double = 0   //滚指采集独立得分
      val personId = tpcardimgmnt.personid
      val ftpPath = hallBatchConfig.ftpPath + "/" + personId
      while(imgDataList.hasNext){
        val imageData = imgDataList.next()
        if(null != imageData.mntData){
          val qualityImage = NativeQualityScore.GetQualityScore(imageData.imgData,imageData.mntData,imageData.fgp,"QualityImage")   //图像打分
          qualityScoreSet(imageData.fgp,qualityImage,qualityScore)  //QualityScore 给打完分的指位赋值
          saveImg(personId, imageData.fgp, imageData.imgData, qualityImage.qualityImg)  //保存原图和红白图
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
      qualityScore.save()

      //根据设置分数判断人员指纹是否达标
      fingerGradeService.isQualified(personId)
    }

  }

  def qualityScoreSet(fgp :Int,qualityImage: QualityImage,qualityScore: QualityScore ): Unit ={

    fgp match {
      case 0 =>
        qualityScore.rmr = qualityImage.qualityScore
      case 1 =>
        qualityScore.rsr = qualityImage.qualityScore
      case 2 =>
        qualityScore.rzr = qualityImage.qualityScore
      case 3 =>
        qualityScore.rhr = qualityImage.qualityScore
      case 4 =>
        qualityScore.rxr = qualityImage.qualityScore
      case 5 =>
        qualityScore.lmr = qualityImage.qualityScore
      case 6 =>
        qualityScore.lsr = qualityImage.qualityScore
      case 7 =>
        qualityScore.lzr = qualityImage.qualityScore
      case 8 =>
        qualityScore.lhr = qualityImage.qualityScore
      case 9 =>
        qualityScore.lxr = qualityImage.qualityScore
      case 10 =>
        qualityScore.rmp = qualityImage.qualityScore
      case 11 =>
        qualityScore.rsp = qualityImage.qualityScore
      case 12 =>
        qualityScore.rzp = qualityImage.qualityScore
      case 13 =>
        qualityScore.rhp = qualityImage.qualityScore
      case 14 =>
        qualityScore.rxp = qualityImage.qualityScore
      case 15 =>
        qualityScore.lmp = qualityImage.qualityScore
      case 16 =>
        qualityScore.lsp = qualityImage.qualityScore
      case 17 =>
        qualityScore.lzp = qualityImage.qualityScore
      case 18 =>
        qualityScore.lhp = qualityImage.qualityScore
      case 19 =>
        qualityScore.lxp = qualityImage.qualityScore
    }
  }

  /**
    * 保存指纹原图和红白图
    */
  def saveImg(personId :String, fgp: Int, origin : Array[Byte], rwImg : Array[Byte]): Unit ={

    val saveOriginImg =  FtpUtil.uploadFile(hallBatchConfig.ftpHost, hallBatchConfig.ftpUserName, hallBatchConfig.ftpPassword, hallBatchConfig.ftpPort, hallBatchConfig.ftpPath + "/" + personId, personId+ "_" + fgp +".bmp", new ByteArrayInputStream(origin))
    val saveRwImg =  FtpUtil.uploadFile(hallBatchConfig.ftpHost, hallBatchConfig.ftpUserName, hallBatchConfig.ftpPassword, hallBatchConfig.ftpPort, hallBatchConfig.ftpPath + "/" + personId, personId+ "_" + fgp +".jpg", new ByteArrayInputStream(rwImg))
    //todo 校验保存是否成功，若失败可记录相关日志
    if(!saveOriginImg) logger.info("保存原图-" + personId+ "_" + fgp +".bmp" + "- 失败")
    if(!saveRwImg) logger.info("保存红白图-" + personId+ "_" + fgp +".jpg" + "- 失败")
  }

}