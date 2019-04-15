package cherish.component.api

import cherish.component.database.jpa.DataBaseConfiguration
import org.apache.tapestry5.ioc.annotations.EagerLoad

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/1
  */
object LocalApiConfigurationModule {

    @EagerLoad
    def buildDataBaseModel: DataBaseConfiguration ={
       val dataBaseConfiguration =  new DataBaseConfiguration()
       dataBaseConfiguration.dataBaseType = 1 //TODO:此处 修改为 从配置文件进行读取
       dataBaseConfiguration
    }
}


