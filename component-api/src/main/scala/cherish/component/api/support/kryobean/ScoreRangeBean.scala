package cherish.component.api.support.kryobean

import scala.collection.mutable.ArrayBuffer

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/4
  */
class ListScoreRangeBean{
  val listScoreRangeBean = ArrayBuffer[ScoreRangeBean]()
}

class ScoreRangeBean {
  var qualityType:Int = _
  var minScore:Int = _
  var maxScore:Int = _
}
