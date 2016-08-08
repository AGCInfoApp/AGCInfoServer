package models.dao

import com.google.inject.{Singleton, Inject}
import models.tables.SlickTables
import org.slf4j.LoggerFactory
import play.api.db.slick.{HasDatabaseConfigProvider, DatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by wangchunze on 2016/8/8.
 */
@Singleton
class MessageDAO @Inject()(
                            protected val dbConfigProvider: DatabaseConfigProvider
                            ) extends HasDatabaseConfigProvider[JdbcProfile] {

  import slick.driver.MySQLDriver.api._
  private val log = LoggerFactory.getLogger(this.getClass)
  private val rMessage = SlickTables.tMessage
  private val chatList = SlickTables.tChatList

  def createMessage(sendId:Long,receiveId:Long,message:String,createTime:Long) = {
    val r = SlickTables.rMessage(
      id = -1l,
      sendId = sendId,
      receiveId = receiveId,
      message = message,
      createTime = createTime
    )
    db.run(rMessage returning rMessage.map(_.id)+=r).mapTo[Long]
  }

  def getMessage(userId:Long,chatUserId:Long,page:Int,pageSize:Int)={
    db.run(rMessage.filter(m=> (m.sendId===userId && m.receiveId===chatUserId) ||
      (m.sendId===chatUserId && m.receiveId===userId)).sortBy(_.createTime.desc).
      drop((page-1)*pageSize).take(pageSize).result)
  }

  def updateChatList(userId:Long,chatUserId:Long,msg:String,createTime:Long)={
    val r = SlickTables.rChatList(
      id = -1l,
      userId = userId,
      chatUserId = chatUserId,
      lastMessage = msg,
      createTime = createTime
    )
    db.run(chatList.filter(t=>t.userId===userId && t.chatUserId===chatUserId).exists.result).flatMap{
      case true=>
        db.run(chatList.filter(t=>t.userId===userId && t.chatUserId===chatUserId).
          map(t=>(t.lastMessage,t.createTime)).update((msg,createTime)))
      case false=>
        db.run(chatList returning chatList.map(_.id)+=r).mapTo[Long]
    }
  }

  def getChatList(userId:Long)={
    db.run(chatList.filter(_.userId===userId).result)
  }

}
