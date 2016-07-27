package models.dao

import com.google.inject.{Inject, Singleton}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.cache.CacheApi
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile

/**
 * Created by 王春泽 on 2016/7/26.
 */
@Singleton
class UserDAO @Inject()(
                       protected val dbConfigProvider:DatabaseConfigProvider,
                       cache: CacheApi
                       )extends HasDatabaseConfigProvider[JdbcProfile] {
  import slick.driver.MySQLDriver.api._
  import play.api.libs.concurrent.Execution.Implicits.defaultContext
  import concurrent.duration._

  private val log=LoggerFactory.getLogger(this.getClass)
  private val User=SlickTables.tUser
  private val friend = SlickTables.tFriend


  /**
   * 根据登录输入查找用户
   * @param login
   * @return
   */
  def findUser(login:String)={
    db.run(User.filter(t=>(t.username===login)||(t.mobile===login)||(t.email===login)).result.headOption)
  }

  def findAdmin(login:String)={
    db.run(User.filter(t=>t.level===3&&((t.username===login)||(t.mobile===login)||(t.email===login))).result.headOption)
  }

  /**
   * 验证用户登录
   * @param login
   * @param password
   * @return
   */
  def checkLogin(login:String,password:String)={
    db.run(User.filter(t=>((t.username===login)||(t.mobile===login)||(t.email===login))
                          &&(t.password===password)).result.headOption)
  }

  /**
   * 修改用户信息
   * @param user
   * @return
   */
  def modifyUserInfo(user:SlickTables.rUser)={
    db.run(User.insertOrUpdate(user).asTry)
  }


  /**
   * 手机注册
   * @return
   */
  def registerByMobile(mobile:String,password:String)={
    db.run(User.map(t=>(t.mobile,t.password)).returning(User.map(_.id))+=(mobile,password)).mapTo[Long]
  }

  /**
   * 邮箱注册
   * @param email
   * @param password
   * @return
   */
  def registerByEmail(email:String,password:String)={
    db.run(User.map(t=>(t.email,t.password)).returning(User.map(_.id))+=(email,password)).mapTo[Long]
  }

  /**
   * 用户名注册
   * @param username
   * @param password
   * @return
   */
  def registerByUsername(username:String,password:String)={
    db.run(User.map(t=>(t.username,t.password)).returning(User.map(_.id))+=(username,password)).mapTo[Long]
  }


  def register(username:String,password:String,mobile:String,createTime:Long)={
    db.run(User.map(t=>(t.nickname,t.username,t.password,t.mobile,t.createTime)).returning(
      User.map(_.id))+=(username,username,password,mobile,createTime)
    ).mapTo[Long]
  }


  def getUserById(userId:Long)=cache.getOrElse(userCacheKey(userId), 30 minutes){
    db.run(User.filter(_.id===userId).result.headOption)
  }


  def followOther(userId:Long,friendId:Long,createTime:Long)={
    val r = SlickTables.rFriend(
      id = -1l,
      userId = userId,
      friendId = friendId,
      timestamp = createTime
    )
    db.run(friend returning friend.map(_.id)+=r).mapTo[Long]
  }


  def getFriend(userId:Long)={
    db.run(friend.filter(_.userId === userId).result)
  }

  def getFans(userId:Long)={
    db.run(friend.filter(_.friendId === userId).result)
  }







  private[this] def userCacheKey(id: Long) = "user\u0001" + id

}
