package controllers

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import models.dao.{UserDAO, MomentDAO}
import org.slf4j.LoggerFactory
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

/**
 * Created by wangchunze on 2016/7/27.
 */

@Singleton
class Moment@Inject()(
                     val momentDAO: MomentDAO,
                     val userDAO: UserDAO
                       )extends Controller with JsonProtocols{
  private val log = LoggerFactory.getLogger(this.getClass)

  def createMoment = Action.async{implicit request =>
    request.body.asJson match{
      case Some(json)=>
        val userId = (json \ "userId").as[Long]
        val token = (json \ "token").as[String]
        val newsId = (json \ "newsId").asOpt[Long].getOrElse(0l)
        val message = (json \ "message").asOpt[String].getOrElse("")
        val pics = (json \ "pics").asOpt[List[String]].getOrElse(List(""))
        val createTime = System.currentTimeMillis()

        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
      case None =>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }
}
