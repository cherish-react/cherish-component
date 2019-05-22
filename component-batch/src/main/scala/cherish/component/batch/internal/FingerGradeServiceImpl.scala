package cherish.component.batch.internal

import cherish.component.batch.service.FingerGradeService
import cherish.component.jpa.{PersonInfo, PersonLevelScore, QualityScore, QualityScoreRange}
import monad.support.services.LoggerSupport

import scala.collection.mutable

/**
  * @author mengxin
  * @since 2019/5/17
  */
class FingerGradeServiceImpl extends FingerGradeService with LoggerSupport{

  override def isQualified(personid: String): Unit ={
    try {
      val personinfoList = PersonInfo.findBy_personid(personid).iterator()
      while (personinfoList.hasNext) {
        val personInfo = personinfoList.next()
        val personLevel = personInfo.personLevel
        //人员等级
        val personLevelScoreList = PersonLevelScore.findBy_level(levelConvert(personLevel))
        if (personLevelScoreList.size() > 0) {
          val personLevelScore = personLevelScoreList.get(0)
          //人员指位等级
          val qualityScoreRangeList = QualityScoreRange.get.iterator()
          val scoreMap = new mutable.HashMap[Int, Int]() //map存放等级 对应的分数
          while (qualityScoreRangeList.hasNext) {
            val qualityScoreRange = qualityScoreRangeList.next()
            scoreMap.put(qualityScoreRange.level, qualityScoreRange.minScore)
          }
          val qualityScore = QualityScore.findBy_cardid().get(0)
          //人员指位分数
          var pQualified: Boolean = true
          //平指是否达标  true达标  false不达标
          var rQualified: Boolean = true //滚指是否达标  true达标  false不达标
          //人员平指或滚指，只要采集了一个就默认全部采集，若有缺少则认为不达标
          if (qualityScore.rmp != null || qualityScore.rsp != null || qualityScore.rzp != null || qualityScore.rhp != null || qualityScore.rxp != null
            || qualityScore.lmp != null || qualityScore.lsp != null || qualityScore.lzp != null || qualityScore.lhp != null || qualityScore.lxp != null) {
            pQualified = (fgpQualified(qualityScore.rmp, scoreMap.get(personLevelScore.rm).get) && fgpQualified(qualityScore.rsp, scoreMap.get(personLevelScore.rs).get) &&
              fgpQualified(qualityScore.rzp, scoreMap.get(personLevelScore.rz).get) && fgpQualified(qualityScore.rhp, scoreMap.get(personLevelScore.rh).get) &&
              fgpQualified(qualityScore.rxp, scoreMap.get(personLevelScore.rx).get) && fgpQualified(qualityScore.lmp, scoreMap.get(personLevelScore.lm).get) &&
              fgpQualified(qualityScore.lsp, scoreMap.get(personLevelScore.ls).get) && fgpQualified(qualityScore.lzp, scoreMap.get(personLevelScore.lz).get) &&
              fgpQualified(qualityScore.lhp, scoreMap.get(personLevelScore.lh).get) && fgpQualified(qualityScore.lxp, scoreMap.get(personLevelScore.lx).get))
          }
          if (qualityScore.rmr != null || qualityScore.rsr != null || qualityScore.rzr != null || qualityScore.rhr != null || qualityScore.rxr != null
            || qualityScore.lmr != null || qualityScore.lsr != null || qualityScore.lzr != null || qualityScore.lhr != null || qualityScore.lxr != null) {
            rQualified = (fgpQualified(qualityScore.rmr, scoreMap.get(personLevelScore.rm).get) && fgpQualified(qualityScore.rsr, scoreMap.get(personLevelScore.rs).get) &&
              fgpQualified(qualityScore.rzr, scoreMap.get(personLevelScore.rz).get) && fgpQualified(qualityScore.rhr, scoreMap.get(personLevelScore.rh).get) &&
              fgpQualified(qualityScore.rxr, scoreMap.get(personLevelScore.rx).get) && fgpQualified(qualityScore.lmr, scoreMap.get(personLevelScore.lm).get) &&
              fgpQualified(qualityScore.lsr, scoreMap.get(personLevelScore.ls).get) && fgpQualified(qualityScore.lzr, scoreMap.get(personLevelScore.lz).get) &&
              fgpQualified(qualityScore.lhr, scoreMap.get(personLevelScore.lh).get) && fgpQualified(qualityScore.lxr, scoreMap.get(personLevelScore.lx).get))
          }
          if (pQualified && rQualified) personInfo.isQualified = 1 else personInfo.isQualified = 0

          personInfo.update()
        } else {
          throw new Exception(levelConvert(personLevel) + "级人员指位等级未设置！")
        }
      }
    }catch {
      case ex:Exception =>
        error(ex.getMessage)
        ex.printStackTrace()
    }


  }

  /**
    * 指纹达标验证方法
    * @return
    */
  def fgpQualified(fgpScore: Double, scoreConfig: Int): Boolean ={
    var result :Boolean = true
    if(null != fgpScore && fgpScore >0){
      result = fgpScore > scoreConfig
    }else{
      result = false
    }
    result
  }

  def levelConvert(level :Int): String ={
    level match {
      case 1 =>
        "A"
      case 2 =>
        "B"
      case 3 =>
        "C"
    }
  }

}
