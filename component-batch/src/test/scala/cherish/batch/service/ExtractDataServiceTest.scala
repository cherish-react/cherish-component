package cherish.batch.service


import cherish.batch.CaseTest
import cherish.component.batch.service.ExtractDataService
import cherish.component.jpa.Information
import org.junit.Test

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/9
  */
class ExtractDataServiceTest extends CaseTest{

  @Test
  def test_getData: Unit ={
      val service  = getService[ExtractDataService]


//    val information = new Information()
//    information.name = "天津市红桥区三条石大街派出所"
//    information.id = UUID.randomUUID().toString.replace("-","")
//    information.status = 0
//    information.code = "120106000001"
//    information.host = "8089"
//    information.initTime = new Date()
//    information.ip = "192.168.1.1"
//    information.isStart = 1
//    service.saveData(information)
  }

  @Test
  def test_select: Unit ={
       val query = Information.findBy_is_Start_and_status("1","0")
       //println(query)
       query.toArray().foreach{
         t =>
           println(t.asInstanceOf[Information].name)
       }
  }

  @Test
  def test_update: Unit ={
    val information = Information.find("cb25f87b5b74426abc4541876645bfa7")
    information.name = "天津市红桥区三条石大街大胡同派出所"
    val service  = getService[ExtractDataService]
    service.updateData(information)
  }
}
