package controllers

import com.google.inject.{Inject, Singleton}
import models.JsonProtocols
import models.dao.GoodsDAO
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import utils.ToolUtil
import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by springlustre on 2016/8/11.
 */
@Singleton
class Goods@Inject()(
  val goodsDAO: GoodsDAO
  )extends Controller with JsonProtocols {

  private val log = LoggerFactory.getLogger(this.getClass)

  def listGoods(page:Int,pageSize:Option[Int])=Action.async{implicit request=>
    val curPage = if(page<1) 1 else page
    val curPageSize = pageSize.getOrElse(20)
    goodsDAO.listGoods(curPage,curPageSize).map{seq=>
      val data = seq.map{res=>
        Json.obj(
          "goodsId"->res.id,
          "goodsPic"->res.pics.split("#").headOption,
          "title"->res.title,
          "price"->ToolUtil.doubleFormat(res.price/100)
        )
      }
      Ok(successResult(Json.obj("data"->data)))
    }
  }


  def getGoodsInfo(goodsId:Long)=Action.async{implicit request=>
    goodsDAO.getGoodsById(goodsId).map{
      case Some(goods)=>
        val data = Json.obj(
          "goodsId"->goods.id,
          "title"->goods.title,
          "price"->ToolUtil.doubleFormat(goods.price /100),
          "pics"->goods.pics.split("#").toList,
          "infoPics"->goods.infopics.split("#").toList,
          "description"->goods.description
        )
        Ok(successResult(Json.obj("data"->data)))
      case None=>
        Ok(ErrorCode.goodsNotExist)
    }
  }




}
