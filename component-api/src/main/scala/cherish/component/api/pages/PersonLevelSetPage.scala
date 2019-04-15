package cherish.component.api.pages

import javax.inject.Inject
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import cherish.component.api.service.PersonLevelSetService
import cherish.component.api.support.KryoSupport
import cherish.component.api.support.kryobean.PersonLevelBean
import org.apache.commons.io.IOUtils
import org.apache.tapestry5.json.JSONObject
import org.apache.tapestry5.util.TextStreamResponse

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/3/27
  */
class PersonLevelSetPage {

  @Inject
  private var personLevelSetService:PersonLevelSetService =_

  @Inject
  private var request:HttpServletRequest = _

  @Inject
  private var response:HttpServletResponse = _


  def onActivate ={

      val param_bytes = IOUtils.toByteArray(request.getInputStream)

      val jSONObject = new JSONObject()
      try{
        if(param_bytes.length <=0){
          throw new Exception("param length error")
        }
        val personLevelBean = KryoSupport.deserializer(param_bytes,new PersonLevelBean)
        personLevelSetService.personLevelSet(personLevelBean.addressCode
                                            ,personLevelBean.raceCode
                                            ,personLevelBean.caseCode
                                            ,personLevelBean.gender
                                            ,personLevelBean.minAge
                                            ,personLevelBean.maxAge
                                            ,personLevelBean.criminalRecord)
        jSONObject.put("success",true)
        jSONObject.put("result","")
      }catch {
        case ex:Exception =>
          jSONObject.put("success",false)
          jSONObject.put("message",ex.getMessage)
      }
    new TextStreamResponse("text/plain", jSONObject.toString)
  }
}
