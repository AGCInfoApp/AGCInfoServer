package models.dao

import com.google.inject.{Inject, Singleton}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.cache.CacheApi
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
 * Created by wangchunze on 2016/7/27.
 */

@Singleton
class MomentDAO@Inject()(
                          protected val dbConfigProvider:DatabaseConfigProvider
                          )extends HasDatabaseConfigProvider[JdbcProfile] {
  import slick.driver.MySQLDriver.api._

  private val log=LoggerFactory.getLogger(this.getClass)

  private val moment = SlickTables.tMoment
  private val comment = SlickTables.tMomentComment
  private val vote = SlickTables.tMomentVote

  def listMoment(userId:Long,page:Int,pageSize:Int)={
    db.run(moment.filter(_.userid===userId).
      sortBy(_.createTime.desc).drop((page-1)*pageSize).take(pageSize).result)
  }

  def getMomentComment(momentId:Long)={
    db.run(comment.filter(_.momentId === momentId).result)
  }

  def getMomentVote(momentId:Long)={
    db.run(vote.filter(_.momentId === momentId).result)
  }

  def createMoment(userId:Long,userName:String,userPic:String,newsId:Long,
                   newsTitle:String,newsPic:String,newsDesc:String, message:String,pics:String,
                   createTime:Long)={
    val r = SlickTables.rMoment(
      id = -1l,
      userid = userId,
      userName = userName,
      userPic = userPic,
      newsId = newsId,
      newsTitle = newsTitle,
      newsPic = newsPic,
      newsDesc = newsDesc,
      message = message,
      pics = pics,
      createTime = createTime
    )
    db.run(moment returning moment.map(_.id)+=r ).mapTo[Long]
  }

  def getMoment(momentId:Long)={
    db.run(moment.filter(_.id === momentId).result.headOption)
  }

  def createComment(momentId:Long,userid:Long,content:String,reUid:Long,createTime:Long)={
    val r = SlickTables.rMomentComment(
      id = -1l,
      momentId = momentId,
      userid = userid,
      commentContent = content,
      reUid = reUid,
      createTime = createTime
    )

    db.run(comment returning comment.map(_.id)+=r ).mapTo[Long]
  }

  def createVote(momentId:Long,userid:Long,userName:String,userPic:String,createTime:Long)={
    val r = SlickTables.rMomentVote(
      id = -1l,
      momentId = momentId,
      userid = userid,
      userName = userName,
      userPic = userPic,
      createTime = createTime
    )
    db.run(vote returning vote.map(_.id)+=r ).mapTo[Long]
  }


  def getUserShare(userId:Long,page:Int,pageSize:Int)={
    db.run(moment.filter(t=>(t.userid===userId) && t.newsId>0l).sortBy(_.createTime.desc).
      drop((page-1)*pageSize).take(pageSize).result)
  }




}
