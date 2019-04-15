package cherish.component.api.internal

import java.util.{Date, UUID}

import cherish.component.api.jpa.QualityScoreRange
import cherish.component.api.service.QualityScoreRangeSetService
import cherish.component.api.support.kryobean.ScoreRangeBean
import monad.support.services.LoggerSupport

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/4
  */
class QualityScoreRangeSetServiceImpl extends QualityScoreRangeSetService with LoggerSupport{
  override def getQualityScoreRange: java.util.ArrayList[QualityScoreRange] = QualityScoreRange.get

  override def setQualityScoreRange(scoreRangeBeans:List[ScoreRangeBean]): Unit = {
    try{
      val sortedScoreRangeBean = scoreRangeBeans.sortWith((a,b) => a.qualityType < b.qualityType)
      val qualityScoreRange = QualityScoreRange.get
      if(qualityScoreRange.size() > 0){
        val sortedQualityScoreRange = qualityScoreRange.asInstanceOf[List[QualityScoreRange]]
            .sortWith((a,b) => a.qualityType < b.qualityType)
        Range(1,6).foreach{
          i =>
            val obj = sortedQualityScoreRange(i)
            obj.minScore = sortedScoreRangeBean(i).minScore
            obj.maxScore = sortedScoreRangeBean(i).maxScore
            obj.inputTime = new Date
            obj.update()
        }
      }else{
        for (i<- 0 to 4){
            val qualityScoreRange = new QualityScoreRange()
            qualityScoreRange.id = UUID.randomUUID().toString.replace("-","")
            qualityScoreRange.qualityType = sortedScoreRangeBean(i).qualityType
            qualityScoreRange.minScore = sortedScoreRangeBean(i).minScore
            qualityScoreRange.maxScore = sortedScoreRangeBean(i).maxScore
            qualityScoreRange.inputTime = new Date()
            qualityScoreRange.save()
        }
      }
    }catch {
      case ex:Exception =>
        logger.error("setQualityScoreRange error:{}",ex.getMessage,ex)
        throw new Exception(ex)
    }
  }
}
