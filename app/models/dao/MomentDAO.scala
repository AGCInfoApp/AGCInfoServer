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

  def getMoment(userId:Long,page:Int,pageSize:Int)={
    db.run(moment.filter(_.userid===userId).
      sortBy(_.createTime.desc).drop((page-1)*pageSize).take(pageSize).result)
  }

  def getMomentComment(momentId:Long)={
    db.run(comment.filter(_.momentId === momentId).result)
  }

  def getMomentVote(momentId:Long)={
    db.run(comment.filter(_.momentId === momentId).result)
  }

  def createMoment(userId:Long,userName:String,userPic:String,newsId:Long,
                   newsTitle:String,newsPic:String, message:String,pics:String,createTime:Long)={
    val r = SlickTables.rMoment(
      id = -1l,
      userid = userId,
      userName = userName,
      userPic = userPic,
      newsId = newsId,
      newsTitle = newsTitle,
      newsPic = newsPic,
      message = message,
      pics = pics,
      createTime = createTime
    )
    db.run(moment returning moment.map(_.id)+=r )
  }

  def getMoment(momentId:Long)={
    db.run(moment.filter(_.id === momentId).result.headOption)
  }




}
