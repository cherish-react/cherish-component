package cherish.component.api.service

import java.util
import java.util.Random

import cherish.component.api.CaseTest
import cherish.component.api.support.kryobean.ScoreRangeBean
import org.junit.Test

import scala.collection.mutable.ArrayBuffer

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/4
  */
class QualityScoreRangeSetServiceTest extends CaseTest{
  @Test
  def test_setQualityScoreRange: Unit ={
    val qualityScoreRangeSetService = getService[QualityScoreRangeSetService]
    val listScoreRangeBean = ArrayBuffer[ScoreRangeBean]()
    for (i<- 1 to 5){
      val scoreRangeBean = new ScoreRangeBean()
        scoreRangeBean.qualityType = i
        scoreRangeBean.minScore = new Random().nextInt(100)
        scoreRangeBean.maxScore = new Random().nextInt(100)
        listScoreRangeBean += scoreRangeBean
    }
    qualityScoreRangeSetService.setQualityScoreRange(listScoreRangeBean.toList)
  }
}
