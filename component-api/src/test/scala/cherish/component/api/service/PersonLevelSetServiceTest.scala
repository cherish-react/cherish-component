package cherish.component.api.service

import cherish.component.api.CaseTest
import org.junit.Test

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */
class PersonLevelSetServiceTest extends CaseTest{

  @Test
  def test_createTable: Unit ={
    println("....")
  }
  @Test
  def test_personLevelSet: Unit ={
    val personLevelSetService = getService[PersonLevelSetService]

     val addressCode = "11,12,13,14,15"
     val raceCode = "23,53,46"
     val caseCode = "101"
     val gender = "1"
     val minAge = "18"
     val maxAge = "65"
     val criminalRecord = "1"
    personLevelSetService.personLevelSet(addressCode,raceCode,caseCode,gender,minAge,maxAge,criminalRecord)
  }
}
