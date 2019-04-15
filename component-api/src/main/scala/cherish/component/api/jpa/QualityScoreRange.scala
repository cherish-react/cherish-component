package cherish.component.api.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/3
  */
object QualityScoreRange extends ActiveRecordInstance[QualityScoreRange]
@Entity
@Table(name = "quality_score_range")
class QualityScoreRange extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "type")
  var qualityType:Int = _
  @Column(name = "min_score")
  var minScore:Int = _
  @Column(name = "max_score")
  var maxScore:Int = _
  @Column(name = "input_time")
  var inputTime:Date = _


  def this(id:String,qualityType:Int,minScore:Int,maxScore:Int,inputTime:Date){
    this()
    this.id = id
    this.qualityType = qualityType
    this.minScore = minScore
    this.maxScore = maxScore
    this.inputTime = inputTime
  }
}
