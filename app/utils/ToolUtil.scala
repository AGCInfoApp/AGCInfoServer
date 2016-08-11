package utils

import java.text.DecimalFormat

/**
 * Created by wangchunze on 2016/5/17.
 */
object ToolUtil {

  def calculateRealtion(tags:List[String],news:String)={
    tags.map{tag=>
      news.split(tag).length-1
    }.sum
  }

  def doubleFormat(data:Double)={
    val df = new DecimalFormat("0.00")
    df.format(data).toDouble
  }


}
