package cherish.component.jpa

import java.util.Date
import javax.persistence.{Column, Entity, Id, Table}
import cherish.component.database.jpa.{ActiveRecord, ActiveRecordInstance}

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/9
  */

object Information extends ActiveRecordInstance[Information]
@Entity
@Table(name = "information")
class Information extends ActiveRecord{
  @Id
  @Column(name="id", unique=true, nullable=false, length=32)
  var id:String = _
  @Column(name = "code" ,length=12)
  var code:String = _
  @Column(name = "host", length=15)
  var host:String = _
  @Column(name="init_time")
  var initTime:Date = _
  @Column(name = "ip",length = 15)
  var ip:String = _
  @Column(name="is_start" ,length = 1)
  var isStart:Int = _
  @Column(name = "name",length = 32)
  var name:String = _
  @Column(name = "status",length = 11)
  var status:Int = _


  def this(id:java.lang.String,code:java.lang.String,host:java.lang.String,initTime:java.util.Date,ip:java.lang.String,isStart:java.lang.Integer,name:java.lang.String,status:java.lang.Integer){
    this()
    this.id = id
    this.code = code
    this.host = host
    this.initTime = initTime
    this.ip = ip
    this.isStart = isStart
    this.name = name
    this.status = status
  }

}
