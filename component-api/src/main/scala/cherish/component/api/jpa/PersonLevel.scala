package cherish.component.api.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}

import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */
object PersonLevel extends ActiveRecordInstance[PersonLevel]
@Entity
@Table(name = "person_level")
class PersonLevel extends ActiveRecord{
  @Id
  @Column(name = "id")
  var id:String = _
  @Column(name = "hukou_dimen_id")
  var hukouDimenId:String = _
  @Column(name = "case_dimen_id")
  var caseDimenId:String = _
  @Column(name = "input_time")
  var inputTime:Date = _
  @Column(name = "flag")
  var flag:Int = _

  def this(id:String
           ,hukouDimenId:String
           ,caseDimenId:String
           ,inputTime:Date
           ,flag:Int){
    this()
    this.id = id
    this.hukouDimenId = hukouDimenId
    this.caseDimenId = caseDimenId
    this.inputTime = inputTime
    this.flag = flag
  }
}
