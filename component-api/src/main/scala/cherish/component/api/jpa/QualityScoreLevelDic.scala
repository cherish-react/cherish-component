package cherish.component.api.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/4
  */
object QualityScoreLevelDic extends ActiveRecordInstance[QualityScoreLevelDic]
@Entity
@Table(name = "quality_score_level_dic")
class QualityScoreLevelDic extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "score_level")
  var level:Int = _
  @Column(name = "level_name")
  var levelName:String = _
  @Column(name = "input_time")
  var inputTime:Date = _

  def this(id:String,level:Int,levelName:String,inputTime:Date){
    this()
    this.id = id
    this.level = level
    this.levelName = levelName
    this.inputTime = inputTime
  }
}
