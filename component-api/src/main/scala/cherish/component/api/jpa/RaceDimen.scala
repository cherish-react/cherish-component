package cherish.component.api.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */
object RaceDimen extends ActiveRecordInstance[RaceDimen]
@Entity
@Table(name = "race_dimen")
class RaceDimen extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "dimen_id")
  var dimenId:String = _
  @Column(name = "race_code")
  var raceCode:String = _
  @Column(name = "input_time")
  var inputTime:Date = _

  def this(id:String,dimenId:String,raceCode:String,inputTime:Date){
    this()
    this.id = id
    this.dimenId = dimenId
    this.raceCode = raceCode
    this.inputTime = inputTime
  }
}
