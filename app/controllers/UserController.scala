package controllers

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import models.dao.UserDAO
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
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
  def getUserInfo(userId:Long,token:String)=Action.async{implicit request=>
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
              userId.toLong,
              nickname,
              mobile,
              email,
              user.username,
              user.password,
              sex,
              birthday,
              pic,
              user.readNum,
              user.commentNum,
              user.level,
              user.createTime,
              user.preference,
              signature
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















}
