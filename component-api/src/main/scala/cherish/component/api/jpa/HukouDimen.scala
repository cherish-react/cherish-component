package cherish.component.api.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */
object HukouDimen extends ActiveRecordInstance[HukouDimen]
@Entity
@Table(name = "hukou_dimen")
class HukouDimen extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "dimen_id")
  var dimenId:String = _
  @Column(name = "address_code")
  var addressCode:String = _
  @Column(name = "input_time")
  var inputTime:Date = _

  def this(id:String,dimenId:String,addressCode:String,inputTime:Date){
    this()
    this.id = id
    this.dimenId = dimenId
    this.addressCode = addressCode
    this.inputTime = inputTime
  }
}
