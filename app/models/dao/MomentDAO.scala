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

}
