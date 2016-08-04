package models.dao

import javax.security.auth.Subject

import com.google.inject.{Inject, Singleton}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * Created by 王春泽 on 2016/4/5.
 */


@Singleton
class NewsDAO @Inject()(
                         protected val dbConfigProvider: DatabaseConfigProvider
                         ) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.driver.MySQLDriver.api._

  private val log = LoggerFactory.getLogger(this.getClass)

  private val news = SlickTables.tNews
  private val comments = SlickTables.tNewsComment


  def listNews(cateId:Int,curPage: Int, pageSize: Int) = {
    db.run(news.filter(_.cateId === cateId).sortBy(_.createTime.desc).drop((curPage - 1) * pageSize).
      take(pageSize).map(t=>(t.id,t.cateId,t.title,t.description,t.thumbnail,
      t.createTime,t.url)).result)
  }

  def getNewsById(id: Long) = {
    db.run(news.filter(_.id === id).result.headOption)
  }

  def getAllNews = {
    db.run(news.result)
  }


  def getRecommentNews(tagList: List[String], source: String) = {
    Future.sequence(tagList.map { tag =>
      db.run(news.filter(w => (w.description like ("%" + tag + "%")) || (w.source === source)).result)
    })
  }

  def updateNewsRelation(id: Long, relation: String) = {
    db.run(news.filter(_.id === id).map(_.relationNews).update(relation))
  }



  def getNewsCommentNum(newsId:Long)={
    db.run(comments.filter(_.newsId===newsId).size.result)
  }


  /**
   *
   * @param searchKey
   * @return
   */
  def searchNews(searchKey: String) = {
     db.run(
      news.filter(t =>
        (t.title like ("%" + searchKey + "%")) ||
        (t.description like ("%" + searchKey + "%"))
        ).result
    )
  }


  def getNewsNum(cateId: Int) = {
    db.run(news.filter(_.cateId === cateId).length.result)
  }


  def deleteNews(newsId: Long) = {
    db.run(news.filter(_.id === newsId).delete)
  }


  def getKeyword() = {
    db.run(news.sortBy(_.createTime desc).take(10).map(_.tags).result)
  }



  /************ comment ******************/


    //创建评价
    def createComment(comment:SlickTables.rNewsComment)={
      db.run((comments returning comments.map(_.id))+=comment).mapTo[Long]
    }

    //news评论数量
    def getSumOfNews(newsId:Long)={
      db.run(comments.filter(c=>c.newsId===newsId).size.result)
    }

    //user的评论数量
    def getSumOfUser(userId:Long)={
      db.run(comments.filter(_.userid===userId).size.result)
    }

    //获取新闻评论 分页
    def getCommentByNewsId(newsId:Long,pageSize:Int,curPage:Int)={
      db.run(comments.filter(c=>c.newsId===newsId).sortBy(_.createTime.desc).drop((curPage-1)*pageSize)
        .take(pageSize).result)
    }

    //获取个人评论
    def getCommentByUser(userId:Long,curPage:Int,pageSize:Int)={
      db.run(comments.filter(_.userid===userId).drop((curPage-1)*pageSize)
        .take(pageSize).result)
    }


}
