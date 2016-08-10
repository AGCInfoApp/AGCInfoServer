package controllers

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import models.dao.UserDAO
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.libs.json.{JsNull, Json}
import play.api.mvc.{Action, Controller}
import utils.SecureUtil
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * Created by 王春泽 on 2016/4/5.
 */

@Singleton
class UserController @Inject()(
                              userDAO:UserDAO
                              )extends Controller with JsonProtocols{
  private val logger = LoggerFactory.getLogger(this.getClass)

  /**
   * @return
   */
  def userLogin=Action.async{implicit request=>
    request.body.asJson match{
      case Some(jsonData)=>
        val login=(jsonData \ "login").as[String]
        val password=(jsonData \ "password").as[String]
        userDAO.findUser(login).flatMap{
          case Some(exist)=>
            userDAO.checkLogin(login,password).map{
              case Some(user)=>
                val timestamp = System.currentTimeMillis()
                val userId = user.id
                val userName = user.username
                val mobile = user.mobile
                val userPwd = user.password
                val token = SecureUtil.getToken(List(userName,mobile,userPwd))
                Ok(successResult(Json.obj(
                  "userId"->userId,
                  "token"->token
                )))
              case None=>
                Ok(ErrorCode.passwordErr)
            }
          case None=>
            Future.successful(Ok(ErrorCode.userNotExist))
        }

      case None=>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }

  /**
   * @return
   */
  def userRegister=Action.async{implicit request=>
      request.body.asJson match{
        case Some(jsonData)=>
          val username=(jsonData \ "username").as[String]
          val password=(jsonData \ "password").as[String]
          val mobile=(jsonData \ "mobile").as[String]
          val createTime = System.currentTimeMillis()
          userDAO.register(username,password,mobile,createTime).map{res=>
            val token = SecureUtil.getToken(List(username,password,mobile))
            if(res>0)
              Ok(successResult(Json.obj(
                "userId"->res,
                "token"->token
              )))
            else
              Ok(ErrorCode.registerErr)
          }

        case None=>
          Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
      }
    }


  /**
   * @return
   */
  def getMyInfo(userId:Long,token:String)=Action.async{implicit request=>
        userDAO.getUserById(userId.toLong).map{
          case Some(user)=>
            Ok(successResult(Json.obj("data"->user)))
          case None=>
            Ok(ErrorCode.userNotExist)
        }
  }


  /**
   * @return
   */
  def editInfo = Action.async { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        val userId = (json \ "userId").as[Long]
        val token = (json \ "token").as[String]
        val nickname = (json \ "nickname").as[String]
        val mobile = (json \ "mobile").as[String]
        val email = (json \ "email").as[String]
        val sex = (json \ "sex").as[String]
        val birthday = (json \ "birthday").as[Long]
        val pic = (json \ "pic").as[String]
        val signature = (json \ "signature").as[String]
        userDAO.getUserById(userId.toLong).flatMap {
          case Some(user) =>
            val u = SlickTables.rUser(
              id = userId.toLong,
              nickname = nickname,
              mobile = mobile,
              email = email,
              username = user.username,
              password = user.password,
              sex = sex,
              birthday = birthday,
              pic = pic,
              readNum = user.readNum,
              commentNum = user.commentNum,
              level = user.level,
              createTime = user.createTime,
              preference = user.preference,
              signature = signature
            )
            userDAO.modifyUserInfo(u).map {
              case Success(_) =>
                Ok(success)
              case Failure(e) =>
                logger.error(e.getMessage)
                Ok(ErrorCode.userEditErr)
            }
          case None =>
            Future.successful(Ok(ErrorCode.userNotExist))
        }
      case None =>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }


  /**
   * to follow some
   * @return
   */
  def followOtherUser = Action.async { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        val userId = (json \ "userId").as[Long]
        val friendId = (json \ "friendId").as[Long]
        val token = (json \ "token").as[String]
        val createTime = System.currentTimeMillis()
        userDAO.followOther(userId, friendId, createTime).map { res =>
            if (res > 0l)
              Ok(success)
            else
              Ok(ErrorCode.followFailed)
        }

      case None =>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }


  def getUserFriend(userId:Long)=Action.async{implicit request=>
    userDAO.getFriend(userId).flatMap{seq=>
      Future.sequence(seq.map{f=>
        userDAO.getUserById(f.friendId).map{
          case Some(user)=>
            Json.obj(
              "userId"->user.id,
              "remarkName"->f.remarkName,
              "nickname"->user.nickname,
              "sex"->user.sex,
              "pic"->user.pic,
              "level"->user.level,
              "signature"->user.signature
            )
          case None =>
            JsNull
        }
      }).map{res=>
        val data = res.filter(t=>t!=JsNull)
        Ok(successResult(Json.obj("data"->data)))
      }
    }
  }

  def getUserFans(userId:Long)=Action.async{implicit request=>
    userDAO.getFans(userId).flatMap{seq=>
      Future.sequence(seq.map{f=>
        userDAO.getUserById(f.userId).map{
          case Some(user)=>
            Json.obj(
              "userId"->user.id,
              "remarkName"->f.remarkName,
              "nickname"->user.nickname,
              "sex"->user.sex,
              "pic"->user.pic,
              "level"->user.level,
              "signature"->user.signature
            )
          case None =>
            JsNull
        }
      }).map{res=>
        val data = res.filter(t=>t!=JsNull)
        Ok(successResult(Json.obj("data"->data)))
      }
    }
  }

  def getUserInfo(userId:Long) = Action.async{implicit request=>
    userDAO.getUserById(userId).map{res=>
      Ok(successResult(Json.obj("data"->res)))
    }
  }



  def searchUser(searchKey:String) = Action.async{implicit request=>
    val userId = try{
      searchKey.toLong
    }catch {
      case _=>
      "0".toLong
    }

    if(userId!=0l){
      userDAO.getUserById(userId).map{
        case Some(user)=>
          val data = List(Json.obj(
            "userId"->user.id,
            "nickname"->user.nickname,
            "sex"->user.sex,
            "pic"->user.pic
          ))
          Ok(successResult(Json.obj("data"->data)))
        case None=>
          Ok(ErrorCode.userNotExist)
      }
    }else{
      userDAO.searchUser(searchKey).map{seq=>
        if(seq.isEmpty){
          Ok(ErrorCode.userNotExist)
        }else{
          val data = seq.map{user=>
            Json.obj(
              "userId"->user.id,
              "nickname"->user.nickname,
              "sex"->user.sex,
              "pic"->user.pic
            )
          }
          Ok(successResult(Json.obj("data"->data)))
        }
      }
    }

  }





















}
