package models.dao

import com.google.inject.{Inject, Singleton}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
 * Created by springlustre on 2016/8/10.
 */
@Singleton
class GoodsDAO@Inject()(
  val dbConfogProvider:DatabaseConfigProvider
  ) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.driver.MySQLDriver.api._
  private val log = LoggerFactory.getLogger(this.getClass)
  private val goods = SlickTables.tGoods

  def listGoods(page:Int,pageSize:Int)={
    db.run(goods.
      drop((page-1)*pageSize).take(pageSize).result)
  }

  def getGoodsById(goodsId:Long)={
    db.run(goods.filter(_.id===goodsId).result.headOption)
  }

}
