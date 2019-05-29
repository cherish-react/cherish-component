package cherish.component.config

import javax.xml.bind.annotation._

import monad.core.config._
import monad.support.services.WebServerConfigSupport


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HallBatchConfig")
@XmlRootElement(name = "hall_batch")
class HallBatchConfig
  extends LogFileSupport
    with WebServerConfigSupport
    with LocalStoreConfigSupport
    with ZkClientConfigSupport
    with HeartbeatConfigSupport{

    @XmlElement(name = "sync")
    var sync: SyncConfig = new SyncConfig

  @XmlElement(name = "rpc")
  var rpc: String = _

  @XmlElement(name = "ftpHost")
  var ftpHost: String = _
  @XmlElement(name = "ftpPort")
  var ftpPort: Int = _
  @XmlElement(name = "ftpUserName")
  var ftpUserName: String = _
  @XmlElement(name = "ftpPassword")
  var ftpPassword: String = _
  @XmlElement(name = "ftpPath")
  var ftpPath: String = _

  @XmlElement(name = "database")
  var db: DatabaseConfig = new DatabaseConfig()


}

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SyncConfig")
class SyncConfig {
    @XmlElement(name = "sync_cron")
    var syncCron: String = _
}
