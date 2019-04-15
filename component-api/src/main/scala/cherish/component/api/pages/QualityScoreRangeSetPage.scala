package cherish.component.api.pages

import javax.inject.Inject
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import cherish.component.api.service.QualityScoreRangeSetService
import cherish.component.api.support.KryoSupport
import cherish.component.api.support.kryobean.ListScoreRangeBean
import org.apache.commons.io.IOUtils
import org.apache.tapestry5.json.JSONObject
import org.apache.tapestry5.util.TextStreamResponse

/**
  * @author <a href="yuchen_1997_200486@126.com">yuchen</a>.
  * @since 2019/4/4
  */
class QualityScoreRangeSetPage {

  @Inject
  private var qualityScoreRangeSetService:QualityScoreRangeSetService = _

  @Inject
  private var request:HttpServletRequest = _

  @Inject
  private var response:HttpServletResponse = _


  def onActivate ={
    val jSONObject = new JSONObject()
    try{
      val param_bytes = IOUtils.toByteArray(request.getInputStream)
      if(param_bytes.length > 0){
        val listScoreRangeBean = KryoSupport.deserializer(param_bytes,new ListScoreRangeBean)
        qualityScoreRangeSetService.setQualityScoreRange(listScoreRangeBean.listScoreRangeBean.toList)
        jSONObject.put("success",true)
        jSONObject.put("result","设置成功")
      }else{
        throw new Exception("传入参数不正确")
      }
    }catch {
      case ex:Exception =>
        jSONObject.put("success",false)
        jSONObject.put("message",ex.getMessage)
    }

    new TextStreamResponse("text/plain", jSONObject.toString)
  }
}
