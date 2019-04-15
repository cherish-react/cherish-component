package cherish.component.api.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */

object CaseDimen extends ActiveRecordInstance[CaseDimen]
@Entity
@Table(name = "case_dimen")
class CaseDimen extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "dimen_id")
  var dimenId:String = _
  @Column(name = "case_code")
  var caseCode:String = _
  @Column(name = "input_time")
  var inputTime:Date = _

  def this(id:String,dimenId:String,caseCode:String,inputTime:Date){
    this()
    this.id = id
    this.dimenId = dimenId
    this.caseCode = caseCode
    this.inputTime = inputTime
  }
}
