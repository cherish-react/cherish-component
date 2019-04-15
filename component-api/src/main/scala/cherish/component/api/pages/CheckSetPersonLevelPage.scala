package cherish.component.api.pages

import javax.inject.Inject
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import cherish.component.api.service.PersonLevelSetService
import org.apache.tapestry5.json.JSONObject
import org.apache.tapestry5.util.TextStreamResponse

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/3
  */
class CheckSetPersonLevelPage {

  @Inject
  private var personLevelSetService:PersonLevelSetService =_

  @Inject
  private var request:HttpServletRequest = _

  @Inject
  private var response:HttpServletResponse = _


  def onActivate ={
    val jSONObject = new JSONObject()
    try{
      jSONObject.put("success",true)
      jSONObject.put("result",personLevelSetService.isSetPersonLevel)
    }catch {
      case ex:Exception =>
        jSONObject.put("success",false)
        jSONObject.put("message",ex.getMessage)
    }
    new TextStreamResponse("text/plain", jSONObject.toString)
  }
}
