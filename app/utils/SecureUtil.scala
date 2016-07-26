package utils

import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory

import scala.util.Random

/**
 * User: Taoz
 * Date: 7/8/2015
 * Time: 8:42 PM
 */
object SecureUtil {


  private val log = LoggerFactory.getLogger(this.getClass)
  val random = new Random(System.currentTimeMillis())

  val chars = Array(
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  )


  def getSecurePassword(password: String, ip: String, timestamp: Long): String = {
    DigestUtils.sha1Hex(DigestUtils.md5Hex(timestamp + password) + ip + timestamp)
  }

  def checkSignature(parameters: List[String], signature: String, secureKey: String) = {
    generateSignature(parameters, secureKey) == signature
  }

  def generateSignature(parameters: List[String], secureKey: String) = {
    val strSeq = (secureKey :: parameters).sorted.mkString("")
//    log.info(s"strSeq:$strSeq  length:${strSeq.length}")
//    log.info(s"singature: ${DigestUtils.sha1Hex(strSeq)}")
    DigestUtils.sha1Hex(strSeq)
  }

  def generateSignatureParameters(parameters: List[String], secureKey: String) = {
    val timestamp = System.currentTimeMillis().toString
    val nonce = nonceStr(6)
    val pList = nonce :: timestamp :: parameters
    val signature = generateSignature(pList, secureKey)
    (timestamp, nonce, signature)
  }

  def nonceStr(length: Int) = {
    val range = chars.length
    (0 until length).map { _ =>
      chars(random.nextInt(range))
    }.mkString("")
  }


  def checkStringSign(str: String, sign: String, secureKey: String) = {
    stringSign(str, secureKey) == sign
  }

  def stringSign(str: String, secureKey: String) = {
    DigestUtils.sha1Hex(secureKey + str)
  }


  def getToken(parameters: List[String])={
    val strSeq = parameters.sorted.mkString("")
    DigestUtils.sha1Hex(strSeq)
  }

  def main(args: Array[String]) {

    val pList = List(
      "10201511253468432",
      "bkpf678kefh5af8d",
      "144491230000411",
      "%E7%BA%A2%E5%8C%85%E9%80%81%E4%B8%8D%E5%81%9C",
      "WEIXIN",
      "osPHijpDqSLW_W3JkePk6hm-PXm0",
      "1024000",
      "20151231",
      "14200416001127",
      "3m44Afz5oP"
    )

    val secureKey = "FLBucky"

    val strA = (secureKey :: pList).sorted.mkString("")

    println(strA)

    val signature = generateSignature(pList, secureKey)

    println(signature)




  }


}
