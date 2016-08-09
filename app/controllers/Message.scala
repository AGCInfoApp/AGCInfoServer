package controllers

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import models.dao.{MessageDAO, UserDAO, RecordDAO, NewsDAO}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsNull, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by wangchunze on 2016/8/8.
 */

@Singleton
class Message@Inject()(
                       val newsDAO:NewsDAO,
                       val messageDAO: MessageDAO,
                       val userDAO: UserDAO
                       ) extends Controller with JsonProtocols {

  private val logger = LoggerFactory.getLogger(this.getClass)

  def createMessage = Action.async{implicit request=>
    request.body.asJson match {
      case Some(json) =>
        val sendId = (json \ "sendId").as[Long]
        val token = (json \ "token").as[String]
        val receiveId = (json \ "receiveId").as[Long]
        val message = (json \ "message").as[String]
        val createTime = System.currentTimeMillis()
        userDAO.getUserById(sendId).flatMap {
          case Some(user)=>
           messageDAO.createMessage(sendId,receiveId,message,createTime).flatMap{res=>
             if(res>0l){
               messageDAO.updateChatList(sendId,receiveId,message,createTime).map{
                 case _=>
                   Ok(success)
               }
             }
             else
               Future.successful(Ok(ErrorCode.sendMsgFailed))
           }
          case None=>
            Future.successful(Ok(ErrorCode.userNotExist))
        }

      case None =>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }


  def getChatList(userId:Long,token:String)=Action.async{implicit request=>
    messageDAO.getChatList(userId).flatMap{seq=>
      Future.sequence(seq.map{chat=>
        userDAO.getUserById(chat.chatUserId).map{
          case Some(user)=>
            Json.obj(
              "chatUserId"->user.id,
              "chatUserName"->user.nickname,
              "chatUserPic"->user.pic,
              "message"->chat.lastMessage,
              "createTime"->chat.createTime
            )
          case None=>
            JsNull
        }
      }).map{data=>
        Ok(successResult(Json.obj("data"->data.filterNot(t=>t==JsNull))))
      }
    }
  }

  def getChatMessage(userId:Long,token:String,chatUserId:Long,
    page:Int,pageSize:Option[Int])=Action.async{implicit request=>
    val curPage = if(page<1) 1 else page
    val curPageSize = pageSize.getOrElse(20)
    messageDAO.getMessage(userId,chatUserId,curPage,curPageSize).map{seq=>
      val data = seq.map{msg=>
        Json.obj(
        "id"->msg.id,
        "sendId"->msg.sendId,
        "receiveId"->msg.receiveId,
        "message"->msg.message,
        "createTime"->msg.createTime
        )
      }
      Ok(successResult(Json.obj("data"->data)))
    }
  }


}



