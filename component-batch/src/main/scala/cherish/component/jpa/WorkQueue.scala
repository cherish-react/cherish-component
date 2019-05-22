package cherish.component.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author mengxin
  * @since 2019/5/22
  */
object WorkQueue extends ActiveRecordInstance[WorkQueue]

@Entity
@Table(name = "work_queue")
class WorkQueue extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "INSERT_TIME")
  var insertTime: Date = _
  @Column(name = "BEGIN_TIME")
  var beginTime: Date = _
  @Column(name = "END_TIME")
  var endTime: Date = _
  @Column(name = "WORK_TYPE")
  var workType: Int = _
  @Column(name = "WORK_STATE")
  var workState: Int = _

  def this(id:String,insertTime:Date,beginTime:Date,endTime:Date,workType:Int,workState:Int){
    this()
    this.id = id
    this.insertTime = insertTime
    this.beginTime = beginTime
    this.endTime = endTime
    this.workType = workType
    this.workState = workState
  }
}
