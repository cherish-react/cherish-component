package cherish.component.pages

import javax.inject.Inject
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import cherish.component.batch.service.BatchIsQualifiedService
import org.apache.tapestry5.json.JSONObject
import org.apache.tapestry5.util.TextStreamResponse

/**
  * @author mengxin
  * @since 2019/5/20
  */
class FingerGradePage {

  @Inject
  private var request:HttpServletRequest = _

  @Inject
  private var response:HttpServletResponse = _

  @Inject
  private var batchIsQualifiedService: BatchIsQualifiedService =_

  def onActivate ={

    val jSONObject = new JSONObject()
    try{
      batchIsQualifiedService.batchIsQualified()
      jSONObject.put("success",true)
      jSONObject.put("message","开始重新判定人员指纹采集是否达标")
    }catch {
      case ex:Exception =>
        jSONObject.put("success",false)
        jSONObject.put("message",ex.getMessage)
    }
    new TextStreamResponse("text/plain", jSONObject.toString)


  }
}
