//package controllers
//
//import com.google.inject.{Inject, Singleton}
//import models.JsonProtocols
//import models.dao.{NewsDAO}
//import models.tables.SlickTables
//import org.slf4j.LoggerFactory
//import play.api.libs.json.Json
//import play.api.mvc.{Action, Controller}
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.concurrent.Future
//
///**
// * Created by 王春泽 on 2016/4/1.
// */
//
//
//@Singleton
//class NewsController@Inject()(
//  val newsDAO:NewsDAO
//  ) extends Controller with JsonProtocols {
//
//  private val logger = LoggerFactory.getLogger(this.getClass)
//  private val pageSize=20  //每页显示的数量
//
//
//
//  def getNewsList(cateId:Int)=Action.async{implicit request=>
//    newsDAO.getNewsList(cateId,1,20).map{res=>
//      val a=res.map{
//          case t:SlickTables.tWangyi=>
//            print(t)
//            val news=new newsObj()
//            news.applyWangyi(t.asInstanceOf[SlickTables.rWangyi])
//            news
//          case _=>
//            println("------")
//            new newsObj()
//      }
//      Ok(successResult(Json.obj("data"->a)))
//    }
//  }
//
//
//
//  /**
//   * @return
//   */
//  def getNewsInfo(cateId:Int,newsId:Long)=Action.async{implicit request=>
//    val userId = request.session.get(SessionKey.userId).getOrElse("0").toLong
//    cateId match{
//      case 2=>
//        newsDAO.getLeifengById(newsId).map{
//          case Some(news)=>
//            if(userId>0){
//              val createTime = System.currentTimeMillis()
//              recordDAO.createRecord(cateId,newsId,userId,createTime,news.tags)
//            }
//            Ok(successResult(Json.obj("data"->news)))
//          case None=>
//            Ok(ErrorCode.newsNotExist)
//        }
//      case _=>
//        newsDAO.getWangyiById(newsId).map{
//          case Some(news)=>
//          if(userId>0){
//            val createTime = System.currentTimeMillis()
//            recordDAO.createRecord(cateId,newsId,userId,createTime,news.tags)
//          }
//          Ok(successResult(Json.obj("data"->news)))
//          case None=>
//            Ok(ErrorCode.newsNotExist)
//        }
//    }
//  }
//
//
//  def getRecommentNews(cateId:Int,id:Long)=Action.async{implicit request=>
//    cateId match{
//      case 2=>
//        newsDAO.getLeifengById(id).flatMap{
//          case Some(news)=>
//            val relation=news.relationNews.split(",").toList
//            if(relation.nonEmpty){
//              Future.sequence(relation.map{r=>
//                newsDAO.getLeifengById(r.toLong)
//              }).map{data=>
//                Ok(successResult(Json.obj("data"->data.take(4))))
//              }
//            }else{
//              val tag=news.tags.split(",").toList
//              newsDAO.getRecommentLeifengNews(tag,news.source).map{res=>
//                val data=res.flatten.distinct.filterNot(_.id==id).take(4)
//                newsDAO.updateLeifengRelation(news.id,data.map(_.id).foldLeft("")((a,b)=>a+","+b).drop(1))
//                Ok(successResult(Json.obj("data"->data)))
//              }
//            }
//
//          case None=>
//            Future.successful(Ok(success))
//        }
//      case _=>
//        newsDAO.getWangyiById(id).flatMap{
//          case Some(news)=>
//            val relation=news.relationNews.split(",").toList
//            if(relation.length>1){
//              Future.sequence(relation.map{r=>
//                newsDAO.getWangyiById(r.toLong)
//              }).map{data=>
//                Ok(successResult(Json.obj("data"->data.take(4))))
//              }
//            }else{
//              val tag=news.tags.split(",").toList
//              newsDAO.getRecommentWangyiNews(tag,news.source).map{res=>
//                val data=res.flatten.distinct.filterNot(_.id==id).take(4)
//                newsDAO.updateWangyiRelation(news.id,data.map(_.id).foldLeft("")((a,b)=>a+","+b).drop(1))
//                Ok(successResult(Json.obj("data"->data)))
//              }
//            }
//          case None=>
//            Future.successful(Ok(success))
//        }
//    }
//  }
//
//
//  def searchNews(searchKey:String)=Action.async{implicit request=>
//    newsDAO.searchNews(searchKey).map{seq=>
//      val data=seq.sortBy(_.createTime).reverse
//      Ok(successResult(Json.obj("data"->data)))
//    }
//  }
//
//
//  def getRecentKeyword=Action.async{implicit request=>
//    newsDAO.getKeyword().map{res=>
//      val keyword=res.flatMap{tags=>
//        tags.split(",")
//      }.take(20)
//      Ok(successResult(Json.obj("data"->keyword)))
//    }
//  }
//
//
//
//
//
//
//  def deleteNews(cateId:Int,newsId:Long)=Action.async{implicit request=>
////    request.session.get(SessionKey.uType) match{
//      Option("3") match {
//      case Some("3") =>
//        newsDAO.deleteNews(cateId,newsId).map{res=>
//          if(res>0){
//            Ok(success)
//          }else{
//            Ok(ErrorCode.deleteFailed)
//          }
//        }
//      case None=>
//        Future.successful(Ok(ErrorCode.hasNotRight))
//    }
//  }
//
//
//
//
//
//}
