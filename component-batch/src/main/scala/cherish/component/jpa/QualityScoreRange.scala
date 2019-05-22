package cherish.component.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author mengxin
  * @since 2019/5/17
  */
object QualityScoreRange extends ActiveRecordInstance[QualityScoreRange]

@Entity
@Table(name = "quality_score_range")
class QualityScoreRange extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "min_score")
  var minScore: Int = _
  @Column(name = "max_score")
  var maxScore: Int = _
  @Column(name = "score_level")
  var level: Int = _
  @Column(name = "name")
  var name: String = _
  @Column(name = "input_time")
  var inputTime: Date = _


  def this(id:String,minScore:Int,maxScore:Int,level:Int,name:String,inputTime:Date){
    this()
    this.id = id
    this.minScore = minScore
    this.maxScore = maxScore
    this.level = level
    this.name = name
    this.inputTime = inputTime
  }
}
