package controllers

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import models.dao.{NewsDAO, UserDAO, MomentDAO}
import org.slf4j.LoggerFactory
import play.api.libs.json.{Json, JsNull}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by wangchunze on 2016/7/27.
 */

@Singleton
class Moment@Inject()(
                     val momentDAO: MomentDAO,
                     val userDAO: UserDAO,
                     val newsDAO: NewsDAO
                       )extends Controller with JsonProtocols{
  private val log = LoggerFactory.getLogger(this.getClass)

  def createMoment = Action.async{implicit request =>
    request.body.asJson match{
      case Some(json)=>
        val userId = (json \ "userId").as[Long]
        val token = (json \ "token").as[String]
        val newsId = (json \ "newsId").asOpt[Long].getOrElse(0l)
        val message = (json \ "message").asOpt[String].getOrElse("")
        val picsList = (json \ "pics").asOpt[List[String]].getOrElse(List(""))
        val createTime = System.currentTimeMillis()
        val pics = picsList.foldLeft("")((sum,a)=>sum+a+"#").dropRight(1)
        userDAO.getUserById(userId).flatMap {
          case Some(user)=>
          if (newsId != 0) {
            newsDAO.getNewsById(newsId).flatMap{
              case Some(news)=>
                momentDAO.createMoment(userId,user.nickname,user.pic,newsId,
                news.title,news.thumbnail,news.description,message,pics,createTime).map{res=>
                  if(res>0l)
                    Ok(success)
                  else
                    Ok(ErrorCode.momentCreateFailed)
                }
              case None=>
                Future.successful(Ok(ErrorCode.newsNotExist))
            }
          } else {
            momentDAO.createMoment(userId,user.nickname,user.pic,newsId,
              "","","",message,pics,createTime).map{res=>
              if(res>0l)
                Ok(success)
              else
                Ok(ErrorCode.momentCreateFailed)
            }
          }
          case None=>
            Future.successful(Ok(ErrorCode.userNotExist))
        }

      case None =>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }


  def listMoment(userId:Long,page:Int,pageSize:Option[Int])=Action.async{implicit request=>
    val curPage = if(page<1) 1 else page
    val curPageSize = pageSize.getOrElse(20)
    momentDAO.listMoment(userId,curPage,curPageSize).flatMap{seq=>
      Future.sequence(seq.map{moment=>
        val voteFuture=momentDAO.getMomentVote(moment.id).flatMap{res=>
          Future.sequence(res.map{vote=>
            userDAO.getUserById(vote.userid).map{
              case Some(user)=>
                Json.obj(
                  "userId"->user.id,
                  "userName"->user.nickname,
                  "userPic"->user.pic
                )
              case None=>
                JsNull
            }
          })
        }

        val commentFuture = momentDAO.getMomentComment(moment.id).flatMap { res =>
          Future.sequence(res.map { comment =>
            userDAO.getUserById(comment.userid).flatMap {
              case Some(user) =>
                userDAO.getUserById(comment.reUid).map {
                  case Some(reUser) =>
                    Json.obj(
                      "userId" -> user.id,
                      "userName" -> user.nickname,
                      "userPic" -> user.pic,
                      "content" -> comment.commentContent,
                      "reId" -> comment.reUid,
                      "reName" -> reUser.nickname
                    )
                  case None =>
                    Json.obj(
                      "userId" -> user.id,
                      "userName" -> user.nickname,
                      "userPic" -> user.pic,
                      "content" -> comment.commentContent,
                      "reId" -> comment.reUid,
                      "reName" -> ""
                    )
                }
              case None =>
                Future(JsNull)
            }
          })
        }
        for{
          vote<-voteFuture
          comment<-commentFuture
        }yield {
          val hasVote = if(vote.exists(t=>(t \ "userId").as[Long]==userId)) 1 else 0
          Json.obj(
            "id"->moment.id,
            "userid"->moment.userid,
            "userName"->moment.userName,
            "userPic"->moment.userPic,
            "newsId"->moment.newsId,
            "newsTitle"->moment.newsTitle,
            "newsPic"->moment.newsPic,
            "newsDesc"->moment.newsDesc,
            "message"->moment.message,
            "createTime"->moment.createTime,
            "hasVote"->hasVote,
            "vote"->vote,
            "comment"->comment
          )
        }
      }).map{data=>
        Ok(successResult(Json.obj("data"->data)))
      }
    }
  }



  def createComment=Action.async{implicit request=>
    request.body.asJson match{
      case Some(json)=>
        val momentId = (json \ "momentId").as[Long]
        val userId = (json \ "userId").as[Long]
        val content = (json \ "content").as[String]
        val reUid = (json \ "reUid").asOpt[Long].getOrElse(0l)
        val createTime = System.currentTimeMillis()
        momentDAO.createComment(momentId,userId,content,reUid,createTime).map{res=>
          if(res>0l)
            Ok(success)
          else
            Ok(ErrorCode.commentCreateFailed)
        }
      case None=>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }

  def createVote = Action.async{implicit request=>
    request.body.asJson match{
      case Some(json)=>
        val momentId = (json \ "momentId").as[Long]
        val userId = (json \ "userId").as[Long]
        val createTime = System.currentTimeMillis()
        userDAO.getUserById(userId).flatMap {
          case Some(user)=>
          momentDAO.createVote(momentId,userId,user.nickname,user.pic, createTime).map{ res =>
            if (res > 0l)
              Ok(success)
            else
              Ok(ErrorCode.commentCreateFailed)
          }
          case None =>
            Future.successful(Ok(ErrorCode.userNotExist))
        }
      case None=>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }

  def getMyShare(userId:Long,token:String,page:Int,pageSize:Option[Int])=Action.async{implicit request=>
    val curPage = if(page<1) 1 else page
    val curPageSize = pageSize.getOrElse(20)
    momentDAO.getUserShare(userId,curPage,curPageSize).map{seq=>
      val data = seq.map{moment=>
        Json.obj(
          "id"->moment.id,
          "newsId"->moment.newsId,
          "newsTitle"->moment.newsTitle,
          "newsPic"->moment.newsPic,
          "createTime"->moment.createTime
        )
      }
      Ok(successResult(Json.obj("data"->data)))
    }
  }

}
