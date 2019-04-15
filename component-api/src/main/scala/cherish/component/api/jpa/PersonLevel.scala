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
  @Column(name = "gender")
  var gender:Int = _
  @Column(name = "race_dimen_id")
  var raceDimenId:String = _
  @Column(name = "min_age")
  var minAge:Int = _
  @Column(name = "max_age")
  var maxAge:Int = _
  @Column(name = "case_dimen_id")
  var caseDimenId:String = _
  @Column(name = "criminal_record")
  var criminalRecord:Int = _
  @Column(name = "input_time")
  var inputTime:Date = _
  @Column(name = "flag")
  var flag:Int = _

  def this(id:String
           ,hukouDimenId:String
           ,gender:Int
           ,raceDimenId:String
           ,minAge:Int
           ,maxAge:Int
           ,caseDimenId:String,criminalRecord:Int,inputTime:Date,flag:Int){
    this()
    this.id = id
    this.hukouDimenId = hukouDimenId
    this.gender = gender
    this.raceDimenId = raceDimenId
    this.minAge = minAge
    this.maxAge = maxAge
    this.caseDimenId = caseDimenId
    this.criminalRecord = criminalRecord
    this.inputTime = inputTime
    this.flag = flag
  }
}
