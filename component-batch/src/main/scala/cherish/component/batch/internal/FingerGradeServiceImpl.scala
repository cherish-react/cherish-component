package cherish.component.batch.internal

import cherish.component.batch.service.{FingerGradeService, JpaSaveOrUpdateService}
import cherish.component.jpa.{PersonInfo, PersonLevelScore, QualityScore, QualityScoreRange}
import monad.support.services.LoggerSupport

import scala.collection.mutable

/**
  * @author mengxin
  * @since 2019/5/17
  */
class FingerGradeServiceImpl(jpaSaveOrUpdateService: JpaSaveOrUpdateService) extends FingerGradeService with LoggerSupport{

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
          val qualityScore = QualityScore.findBy_cardid(personid).get(0)  //人员指位分数
          var pQualified: Boolean = true  //平指是否达标  true达标  false不达标
          var rQualified: Boolean = true //滚指是否达标  true达标  false不达标
          //人员平指或滚指，只要采集了一个就默认全部采集，若有缺少则认为不达标
          if (qualityScore.rmp != 0 || qualityScore.rsp != 0 || qualityScore.rzp != 0 || qualityScore.rhp != 0 || qualityScore.rxp != 0
            || qualityScore.lmp != 0 || qualityScore.lsp != 0 || qualityScore.lzp != 0 || qualityScore.lhp != 0 || qualityScore.lxp != 0) {
            pQualified = (fgpQualified(qualityScore.rmp, scoreMap.get(personLevelScore.rm).get) && fgpQualified(qualityScore.rsp, scoreMap.get(personLevelScore.rs).get) &&
              fgpQualified(qualityScore.rzp, scoreMap.get(personLevelScore.rz).get) && fgpQualified(qualityScore.rhp, scoreMap.get(personLevelScore.rh).get) &&
              fgpQualified(qualityScore.rxp, scoreMap.get(personLevelScore.rx).get) && fgpQualified(qualityScore.lmp, scoreMap.get(personLevelScore.lm).get) &&
              fgpQualified(qualityScore.lsp, scoreMap.get(personLevelScore.ls).get) && fgpQualified(qualityScore.lzp, scoreMap.get(personLevelScore.lz).get) &&
              fgpQualified(qualityScore.lhp, scoreMap.get(personLevelScore.lh).get) && fgpQualified(qualityScore.lxp, scoreMap.get(personLevelScore.lx).get))
          }
          if (qualityScore.rmr != 0 || qualityScore.rsr != 0 || qualityScore.rzr != 0 || qualityScore.rhr != 0 || qualityScore.rxr != 0
            || qualityScore.lmr != 0 || qualityScore.lsr != 0 || qualityScore.lzr != 0 || qualityScore.lhr != 0 || qualityScore.lxr != 0) {
            rQualified = (fgpQualified(qualityScore.rmr, scoreMap.get(personLevelScore.rm).get) && fgpQualified(qualityScore.rsr, scoreMap.get(personLevelScore.rs).get) &&
              fgpQualified(qualityScore.rzr, scoreMap.get(personLevelScore.rz).get) && fgpQualified(qualityScore.rhr, scoreMap.get(personLevelScore.rh).get) &&
              fgpQualified(qualityScore.rxr, scoreMap.get(personLevelScore.rx).get) && fgpQualified(qualityScore.lmr, scoreMap.get(personLevelScore.lm).get) &&
              fgpQualified(qualityScore.lsr, scoreMap.get(personLevelScore.ls).get) && fgpQualified(qualityScore.lzr, scoreMap.get(personLevelScore.lz).get) &&
              fgpQualified(qualityScore.lhr, scoreMap.get(personLevelScore.lh).get) && fgpQualified(qualityScore.lxr, scoreMap.get(personLevelScore.lx).get))
          }
          if (pQualified && rQualified) personInfo.isQualified = 1 else personInfo.isQualified = 0

          jpaSaveOrUpdateService.updatePersonInfo(personInfo)
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
  def fgpQualified(fgpScore: Float, scoreConfig: Int): Boolean ={
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
