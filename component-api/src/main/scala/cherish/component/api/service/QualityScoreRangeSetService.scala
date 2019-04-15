package cherish.component.api.service

import cherish.component.api.jpa.QualityScoreRange
import cherish.component.api.support.kryobean.ScoreRangeBean
import org.springframework.transaction.annotation.Transactional

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/4
  */
trait QualityScoreRangeSetService {

    def getQualityScoreRange:java.util.ArrayList[QualityScoreRange]

    @Transactional
    def setQualityScoreRange(scoreRangeBeans:List[ScoreRangeBean])
}
