package cherish.component.api.internal

import java.util.{Date, UUID}

import cherish.component.api.jpa.{CaseDimen, HukouDimen, PersonLevel, RaceDimen}
import cherish.component.api.service.PersonLevelSetService


/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */
class PersonLevelSetServiceImpl extends PersonLevelSetService{

  override def personLevelSet(addressCode:String
                     ,raceCode:String
                     ,caseCode:String
                     ,gender:String
                     ,minAge:String
                     ,maxAge:String
                     ,criminalRecord:String): Unit ={
      var addressDimenId = ""
      var raceDimenId = ""
      var caseDimenId = ""
      var genderStr = ""
      var minAgeStr = ""
      var maxAgeStr = ""
      var criminalRecordStr = ""

      val personLevelList =  PersonLevel.findBy_flag("1")
      personLevelList.toArray.foreach{
        t =>
          val personLevel = t.asInstanceOf[PersonLevel]
          personLevel.flag = 0
          personLevel.update()
      }

      if(addressCode.length > 0){
        addressDimenId = UUID.randomUUID().toString.replace("-","")
        addressCode.split(',').foreach{
          t =>
            val hukouDimen = new HukouDimen()
            hukouDimen.id = UUID.randomUUID().toString.replace("-","")
            hukouDimen.dimenId = addressDimenId
            hukouDimen.addressCode = t
            hukouDimen.inputTime = new Date()
            hukouDimen.save()
        }

      }
      if(raceCode.length > 0) {
        raceDimenId = UUID.randomUUID().toString.replace("-","")
        raceCode.split(",").foreach{
          t =>
            val raceDimen = new RaceDimen()
            raceDimen.id = UUID.randomUUID().toString.replace("-","")
            raceDimen.dimenId = raceDimenId
            raceDimen.raceCode = t
            raceDimen.inputTime = new Date()
            raceDimen.save()
        }

      }
      if(caseCode.length > 0){
        caseDimenId = UUID.randomUUID().toString.replace("-","")
        caseCode.split(',').foreach{
          t =>
            val caseDimen = new CaseDimen()
            caseDimen.id = UUID.randomUUID().toString.replace("-","")
            caseDimen.dimenId = caseDimenId
            caseDimen.caseCode = t
            caseDimen.inputTime = new Date()
            caseDimen.save()
        }
      }
      if(gender.length > 0){
        genderStr = gender
      }

      if(minAge.length > 0){
        minAgeStr = minAge
      }

      if(maxAge.length > 0){
        maxAgeStr = maxAge
      }

      if(criminalRecord.length >0){
        criminalRecordStr = criminalRecord
      }

      val personLevel = new PersonLevel()
      personLevel.id = UUID.randomUUID().toString.replace("-","")
      personLevel.hukouDimenId = addressDimenId
      personLevel.gender = genderStr.toInt
      personLevel.raceDimenId = raceDimenId
      personLevel.minAge = minAgeStr.toInt
      personLevel.maxAge = maxAgeStr.toInt
      personLevel.caseDimenId = caseDimenId
      personLevel.criminalRecord = criminalRecordStr.toInt
      personLevel.inputTime = new Date
      personLevel.flag = 1
      personLevel.save()

  }

  override def isSetPersonLevel: Int = PersonLevel.findBy_flag("1").size
}
