package controllers

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import models.dao.{UserDAO, RecordDAO, NewsDAO}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by 王春泽 on 2016/4/1.
 */


@Singleton
class NewsController@Inject()(
                               val newsDAO:NewsDAO,
                               val recordDAO: RecordDAO,
                               val userDAO: UserDAO
  ) extends Controller with JsonProtocols {

  private val logger = LoggerFactory.getLogger(this.getClass)
  private val pageSize=20  //每页显示的数量


  def listNews(cateId: Int, page: Int, pageSize: Option[Int]) =
    Action.async { implicit request =>
      val curPage = if (page < 1) 1 else page
      val curPageSize = pageSize.getOrElse(20)
      newsDAO.listNews(cateId, curPage, curPageSize).map { seq =>
        val data = seq.map { t =>
          Json.obj(
            "newsId" -> t._1,
            "cateId" -> t._2,
            "title" -> t._3,
            "description" -> t._4,
            "thumbnail" -> t._5,
            "createTime" -> t._6,
            "url" -> t._7
          )
        }
        Ok(successResult(Json.obj("data" -> data)))
      }
    }


  def getNewsInfo(newsId:Long,userId:Long)=Action.async{implicit request=>
    newsDAO.getNewsById(newsId).map{
      case Some(news) =>
        val cateId = news.cateId
        val createTime = System.currentTimeMillis()
        recordDAO.createRecord(cateId,newsId,userId,createTime,news.tags)
        Ok(successResult(Json.obj("data"->news)))
      case None =>
        Ok(ErrorCode.newsNotExist)
    }
  }


  def getRecommentNews(newsId: Long) = Action.async { implicit request =>
    newsDAO.getNewsById(newsId).flatMap {
      case Some(news) =>
        val relation = news.relationNews.split(",").toList
        if (relation.length > 1) {
          Future.sequence(relation.map { r =>
            newsDAO.getNewsById(r.toLong)
          }).map { data =>
            Ok(successResult(Json.obj("data" -> data.take(4))))
          }
        } else {
          val tag = news.tags.split(",").toList
          newsDAO.getRecommentNews(tag, news.source).map { res =>
            val data = res.flatten.distinct.filterNot(_.id == newsId).take(4)
            newsDAO.updateNewsRelation(news.id, data.map(_.id).foldLeft("")((a, b) => a + "," + b).drop(1))
            Ok(successResult(Json.obj("data" -> data)))
          }
        }
      case None =>
        Future.successful(Ok(success))
    }
  }


  def searchNews(searchKey:String)=Action.async{implicit request=>
    newsDAO.searchNews(searchKey).map{seq=>
      val data=seq.sortBy(_.createTime).reverse
      Ok(successResult(Json.obj("data"->data)))
    }
  }


  def getRecentKeyword=Action.async{implicit request=>
    newsDAO.getKeyword().map{res=>
      val keyword=res.flatMap{tags=>
        tags.split(",")
      }.take(20)
      Ok(successResult(Json.obj("data"->keyword)))
    }
  }


  /***********  comment  ***********/

  def createComment = Action.async { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        val userId = (json \ "userId").as[Long]
        val newsId = (json \ "newsId").as[Long]
        val content = (json \ "content").as[String]
        val reId = (json \ "reUid").as[Long]
        val createTime = System.currentTimeMillis()
        userDAO.getUserById(userId).flatMap {
          case Some(user)=>
          val c = SlickTables.rNewsComment(
            id = -1l,
            userid = userId,
            userName = user.nickname,
            userPic = user.pic,
            newsId = newsId,
            content = content,
            reId = reId,
            createTime = createTime
          )
          newsDAO.createComment(c).map { res =>
            if (res > 0l) {
              Ok(success)
            } else {
              Ok(ErrorCode.commentCreateFailed)
            }
          }
          case None=>
            Future.successful(Ok(ErrorCode.userNotExist))
        }

      case None =>
        Future.successful(Ok(ErrorCode.requestAsJsonEmpty))
    }
  }


    def getCommentByNews(newsId:Long,page:Int,pageSize:Option[Int])=Action.async{implicit request=>
      newsDAO.getSumOfNews(newsId).flatMap{cnt=>
        val curPage = if(page<1) 1 else page
        val curPageSize = pageSize.getOrElse(20)
        val pageCnt = if(cnt%curPageSize==0) cnt/curPageSize else cnt/curPageSize + 1
        newsDAO.getCommentByNewsId(newsId,curPageSize,curPage).map{comment=>
          Ok(successResult(Json.obj("data"->comment,"curPage"->curPage,"pageCnt"->pageCnt)))
        }
      }
    }




  /***************** about collection *********************/
  def createCollect()=Action.async{

  }




/************    for admin  **************/
  def deleteNews(newsId:Long)=Action.async{implicit request=>
    request.session.get(SessionKey.uType) match{
      case Some("3") =>
        newsDAO.deleteNews(newsId).map{res=>
          if(res>0){
            Ok(success)
          }else{
            Ok(ErrorCode.deleteFailed)
          }
        }
      case None=>
        Future.successful(Ok(ErrorCode.hasNotRight))
    }
  }





}
