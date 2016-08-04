package controllers

import java.io.File

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by wangchunze on 2016/8/4.
 */
@Singleton
class Service@Inject()(

                        )extends Controller with JsonProtocols{

  def uploadPic = Action.async { implicit request =>
    try {
      request.body.asMultipartFormData match {
        case Some(multiForm) =>
          if (multiForm.file("image").isDefined) {
            val file = multiForm.file("image").get.ref
            val fName = multiForm.file("image").get.filename.split("\\.").head
            val fileType =multiForm.file("image").get.filename.split("\\.")(1)
            val fileName = System.currentTimeMillis() + fName+"." + fileType
            val dir = new File(System.getProperty("user.home") + "/images")
            if (!dir.exists() && !dir.isDirectory()) dir.mkdir()
            val data = file.moveTo(new File(dir.getCanonicalPath + "/" + fileName))
            Future(Ok(successResult(Json.obj("data"->data.getPath))))
          } else {
            Future(Ok(ErrorCode.uploadPicEmpty))
          }
        case None =>
          Future(Ok(ErrorCode.uploadPicEmpty))
      }
    } catch {
      case e: Exception =>
        Future(Ok(ErrorCode.uploadPicFailed))
    }
  }


}
