package models.tables

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.driver.MySQLDriver
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Array(tCollection.schema, tComment.schema, tFriend.schema, tMoment.schema, tMomentComment.schema, tReadingRecord.schema, tUser.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tCollection
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param cateId Database column cate_id SqlType(BIGINT), Default(0)
   *  @param newsId Database column news_id SqlType(BIGINT), Default(0)
   *  @param newsTitle Database column news_title SqlType(VARCHAR), Length(200,true), Default()
   *  @param userId Database column user_id SqlType(BIGINT), Default(0)
   *  @param createTime Database column create_time SqlType(BIGINT), Default(0)
   *  @param tags Database column tags SqlType(VARCHAR), Length(200,true) */
  case class rCollection(id: Long, cateId: Long = 0L, newsId: Long = 0L, newsTitle: String = "", userId: Long = 0L, createTime: Long = 0L, tags: String)
  /** GetResult implicit for fetching rCollection objects using plain SQL queries */
  implicit def GetResultrCollection(implicit e0: GR[Long], e1: GR[String]): GR[rCollection] = GR{
    prs => import prs._
    rCollection.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[Long], <<[Long], <<[String]))
  }
  /** Table description of table collection. Objects of this class serve as prototypes for rows in queries. */
  class tCollection(_tableTag: Tag) extends Table[rCollection](_tableTag, "collection") {
    def * = (id, cateId, newsId, newsTitle, userId, createTime, tags) <> (rCollection.tupled, rCollection.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(cateId), Rep.Some(newsId), Rep.Some(newsTitle), Rep.Some(userId), Rep.Some(createTime), Rep.Some(tags)).shaped.<>({r=>import r._; _1.map(_=> rCollection.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column cate_id SqlType(BIGINT), Default(0) */
    val cateId: Rep[Long] = column[Long]("cate_id", O.Default(0L))
    /** Database column news_id SqlType(BIGINT), Default(0) */
    val newsId: Rep[Long] = column[Long]("news_id", O.Default(0L))
    /** Database column news_title SqlType(VARCHAR), Length(200,true), Default() */
    val newsTitle: Rep[String] = column[String]("news_title", O.Length(200,varying=true), O.Default(""))
    /** Database column user_id SqlType(BIGINT), Default(0) */
    val userId: Rep[Long] = column[Long]("user_id", O.Default(0L))
    /** Database column create_time SqlType(BIGINT), Default(0) */
    val createTime: Rep[Long] = column[Long]("create_time", O.Default(0L))
    /** Database column tags SqlType(VARCHAR), Length(200,true) */
    val tags: Rep[String] = column[String]("tags", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table tCollection */
  lazy val tCollection = new TableQuery(tag => new tCollection(tag))

  /** Entity class storing rows of table tComment
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param userid Database column userid SqlType(BIGINT), Default(0)
   *  @param userName Database column user_name SqlType(VARCHAR), Length(50,true), Default()
   *  @param userPic Database column user_pic SqlType(VARCHAR), Length(300,true), Default()
   *  @param newsId Database column news_id SqlType(BIGINT)
   *  @param content Database column content SqlType(VARCHAR), Length(3000,true), Default()
   *  @param reId Database column re_id SqlType(BIGINT), Default(0)
   *  @param createTime Database column create_time SqlType(BIGINT), Default(0) */
  case class rComment(id: Long, userid: Long = 0L, userName: String = "", userPic: String = "", newsId: Long, content: String = "", reId: Long = 0L, createTime: Long = 0L)
  /** GetResult implicit for fetching rComment objects using plain SQL queries */
  implicit def GetResultrComment(implicit e0: GR[Long], e1: GR[String]): GR[rComment] = GR{
    prs => import prs._
    rComment.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[Long], <<[String], <<[Long], <<[Long]))
  }
  /** Table description of table comment. Objects of this class serve as prototypes for rows in queries. */
  class tComment(_tableTag: Tag) extends Table[rComment](_tableTag, "comment") {
    def * = (id, userid, userName, userPic, newsId, content, reId, createTime) <> (rComment.tupled, rComment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userid), Rep.Some(userName), Rep.Some(userPic), Rep.Some(newsId), Rep.Some(content), Rep.Some(reId), Rep.Some(createTime)).shaped.<>({r=>import r._; _1.map(_=> rComment.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column userid SqlType(BIGINT), Default(0) */
    val userid: Rep[Long] = column[Long]("userid", O.Default(0L))
    /** Database column user_name SqlType(VARCHAR), Length(50,true), Default() */
    val userName: Rep[String] = column[String]("user_name", O.Length(50,varying=true), O.Default(""))
    /** Database column user_pic SqlType(VARCHAR), Length(300,true), Default() */
    val userPic: Rep[String] = column[String]("user_pic", O.Length(300,varying=true), O.Default(""))
    /** Database column news_id SqlType(BIGINT) */
    val newsId: Rep[Long] = column[Long]("news_id")
    /** Database column content SqlType(VARCHAR), Length(3000,true), Default() */
    val content: Rep[String] = column[String]("content", O.Length(3000,varying=true), O.Default(""))
    /** Database column re_id SqlType(BIGINT), Default(0) */
    val reId: Rep[Long] = column[Long]("re_id", O.Default(0L))
    /** Database column create_time SqlType(BIGINT), Default(0) */
    val createTime: Rep[Long] = column[Long]("create_time", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tComment */
  lazy val tComment = new TableQuery(tag => new tComment(tag))

  /** Entity class storing rows of table tFriend
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param userId Database column user_id SqlType(BIGINT), Default(0)
   *  @param friendId Database column friend_id SqlType(BIGINT), Default(0)
   *  @param remarkName Database column remark_name SqlType(VARCHAR), Length(50,true), Default()
   *  @param timestamp Database column timestamp SqlType(BIGINT), Default(0) */
  case class rFriend(id: Long, userId: Long = 0L, friendId: Long = 0L, remarkName: String = "", timestamp: Long = 0L)
  /** GetResult implicit for fetching rFriend objects using plain SQL queries */
  implicit def GetResultrFriend(implicit e0: GR[Long], e1: GR[String]): GR[rFriend] = GR{
    prs => import prs._
    rFriend.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[Long]))
  }
  /** Table description of table friend. Objects of this class serve as prototypes for rows in queries. */
  class tFriend(_tableTag: Tag) extends Table[rFriend](_tableTag, "friend") {
    def * = (id, userId, friendId, remarkName, timestamp) <> (rFriend.tupled, rFriend.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userId), Rep.Some(friendId), Rep.Some(remarkName), Rep.Some(timestamp)).shaped.<>({r=>import r._; _1.map(_=> rFriend.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column user_id SqlType(BIGINT), Default(0) */
    val userId: Rep[Long] = column[Long]("user_id", O.Default(0L))
    /** Database column friend_id SqlType(BIGINT), Default(0) */
    val friendId: Rep[Long] = column[Long]("friend_id", O.Default(0L))
    /** Database column remark_name SqlType(VARCHAR), Length(50,true), Default() */
    val remarkName: Rep[String] = column[String]("remark_name", O.Length(50,varying=true), O.Default(""))
    /** Database column timestamp SqlType(BIGINT), Default(0) */
    val timestamp: Rep[Long] = column[Long]("timestamp", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tFriend */
  lazy val tFriend = new TableQuery(tag => new tFriend(tag))

  /** Entity class storing rows of table tMoment
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param userid Database column userid SqlType(BIGINT), Default(0)
   *  @param userName Database column user_name SqlType(VARCHAR), Length(50,true), Default()
   *  @param userPic Database column user_pic SqlType(VARCHAR), Length(300,true), Default()
   *  @param newsId Database column news_id SqlType(BIGINT)
   *  @param newsTitle Database column news_title SqlType(VARCHAR), Length(200,true), Default()
   *  @param newsPic Database column news_pic SqlType(VARCHAR), Length(300,true), Default()
   *  @param newsDesc Database column news_desc SqlType(VARCHAR), Length(300,true), Default()
   *  @param message Database column message SqlType(VARCHAR), Length(2000,true), Default()
   *  @param pics Database column pics SqlType(VARCHAR), Length(3000,true), Default()
   *  @param createTime Database column create_time SqlType(BIGINT), Default(0) */
  case class rMoment(id: Long, userid: Long = 0L, userName: String = "", userPic: String = "", newsId: Long, newsTitle: String = "", newsPic: String = "", newsDesc: String = "", message: String = "", pics: String = "", createTime: Long = 0L)
  /** GetResult implicit for fetching rMoment objects using plain SQL queries */
  implicit def GetResultrMoment(implicit e0: GR[Long], e1: GR[String]): GR[rMoment] = GR{
    prs => import prs._
    rMoment.tupled((<<[Long], <<[Long], <<[String], <<[String], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Long]))
  }
  /** Table description of table moment. Objects of this class serve as prototypes for rows in queries. */
  class tMoment(_tableTag: Tag) extends Table[rMoment](_tableTag, "moment") {
    def * = (id, userid, userName, userPic, newsId, newsTitle, newsPic, newsDesc, message, pics, createTime) <> (rMoment.tupled, rMoment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(userid), Rep.Some(userName), Rep.Some(userPic), Rep.Some(newsId), Rep.Some(newsTitle), Rep.Some(newsPic), Rep.Some(newsDesc), Rep.Some(message), Rep.Some(pics), Rep.Some(createTime)).shaped.<>({r=>import r._; _1.map(_=> rMoment.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column userid SqlType(BIGINT), Default(0) */
    val userid: Rep[Long] = column[Long]("userid", O.Default(0L))
    /** Database column user_name SqlType(VARCHAR), Length(50,true), Default() */
    val userName: Rep[String] = column[String]("user_name", O.Length(50,varying=true), O.Default(""))
    /** Database column user_pic SqlType(VARCHAR), Length(300,true), Default() */
    val userPic: Rep[String] = column[String]("user_pic", O.Length(300,varying=true), O.Default(""))
    /** Database column news_id SqlType(BIGINT) */
    val newsId: Rep[Long] = column[Long]("news_id")
    /** Database column news_title SqlType(VARCHAR), Length(200,true), Default() */
    val newsTitle: Rep[String] = column[String]("news_title", O.Length(200,varying=true), O.Default(""))
    /** Database column news_pic SqlType(VARCHAR), Length(300,true), Default() */
    val newsPic: Rep[String] = column[String]("news_pic", O.Length(300,varying=true), O.Default(""))
    /** Database column news_desc SqlType(VARCHAR), Length(300,true), Default() */
    val newsDesc: Rep[String] = column[String]("news_desc", O.Length(300,varying=true), O.Default(""))
    /** Database column message SqlType(VARCHAR), Length(2000,true), Default() */
    val message: Rep[String] = column[String]("message", O.Length(2000,varying=true), O.Default(""))
    /** Database column pics SqlType(VARCHAR), Length(3000,true), Default() */
    val pics: Rep[String] = column[String]("pics", O.Length(3000,varying=true), O.Default(""))
    /** Database column create_time SqlType(BIGINT), Default(0) */
    val createTime: Rep[Long] = column[Long]("create_time", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tMoment */
  lazy val tMoment = new TableQuery(tag => new tMoment(tag))

  /** Entity class storing rows of table tMomentComment
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param momentId Database column moment_id SqlType(BIGINT), Default(0)
   *  @param userid Database column userid SqlType(BIGINT), Default(0)
   *  @param userName Database column user_name SqlType(VARCHAR), Length(50,true), Default()
   *  @param userPic Database column user_pic SqlType(VARCHAR), Length(300,true), Default()
   *  @param commentType Database column comment_type SqlType(INT), Default(0)
   *  @param commentContent Database column comment_content SqlType(VARCHAR), Length(2000,true), Default()
   *  @param reUid Database column re_uid SqlType(BIGINT), Default(0)
   *  @param reUname Database column re_uname SqlType(VARCHAR), Length(100,true), Default()
   *  @param createTime Database column create_time SqlType(BIGINT), Default(0) */
  case class rMomentComment(id: Long, momentId: Long = 0L, userid: Long = 0L, userName: String = "", userPic: String = "", commentType: Int = 0, commentContent: String = "", reUid: Long = 0L, reUname: String = "", createTime: Long = 0L)
  /** GetResult implicit for fetching rMomentComment objects using plain SQL queries */
  implicit def GetResultrMomentComment(implicit e0: GR[Long], e1: GR[String], e2: GR[Int]): GR[rMomentComment] = GR{
    prs => import prs._
    rMomentComment.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[Int], <<[String], <<[Long], <<[String], <<[Long]))
  }
  /** Table description of table moment_comment. Objects of this class serve as prototypes for rows in queries. */
  class tMomentComment(_tableTag: Tag) extends Table[rMomentComment](_tableTag, "moment_comment") {
    def * = (id, momentId, userid, userName, userPic, commentType, commentContent, reUid, reUname, createTime) <> (rMomentComment.tupled, rMomentComment.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(momentId), Rep.Some(userid), Rep.Some(userName), Rep.Some(userPic), Rep.Some(commentType), Rep.Some(commentContent), Rep.Some(reUid), Rep.Some(reUname), Rep.Some(createTime)).shaped.<>({r=>import r._; _1.map(_=> rMomentComment.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column moment_id SqlType(BIGINT), Default(0) */
    val momentId: Rep[Long] = column[Long]("moment_id", O.Default(0L))
    /** Database column userid SqlType(BIGINT), Default(0) */
    val userid: Rep[Long] = column[Long]("userid", O.Default(0L))
    /** Database column user_name SqlType(VARCHAR), Length(50,true), Default() */
    val userName: Rep[String] = column[String]("user_name", O.Length(50,varying=true), O.Default(""))
    /** Database column user_pic SqlType(VARCHAR), Length(300,true), Default() */
    val userPic: Rep[String] = column[String]("user_pic", O.Length(300,varying=true), O.Default(""))
    /** Database column comment_type SqlType(INT), Default(0) */
    val commentType: Rep[Int] = column[Int]("comment_type", O.Default(0))
    /** Database column comment_content SqlType(VARCHAR), Length(2000,true), Default() */
    val commentContent: Rep[String] = column[String]("comment_content", O.Length(2000,varying=true), O.Default(""))
    /** Database column re_uid SqlType(BIGINT), Default(0) */
    val reUid: Rep[Long] = column[Long]("re_uid", O.Default(0L))
    /** Database column re_uname SqlType(VARCHAR), Length(100,true), Default() */
    val reUname: Rep[String] = column[String]("re_uname", O.Length(100,varying=true), O.Default(""))
    /** Database column create_time SqlType(BIGINT), Default(0) */
    val createTime: Rep[Long] = column[Long]("create_time", O.Default(0L))
  }
  /** Collection-like TableQuery object for table tMomentComment */
  lazy val tMomentComment = new TableQuery(tag => new tMomentComment(tag))

  /** Entity class storing rows of table tReadingRecord
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param cateId Database column cate_id SqlType(BIGINT), Default(0)
   *  @param newsId Database column news_id SqlType(BIGINT), Default(0)
   *  @param newsTitle Database column news_title SqlType(VARCHAR), Length(200,true), Default()
   *  @param userId Database column user_id SqlType(BIGINT), Default(0)
   *  @param createTime Database column create_time SqlType(BIGINT), Default(0)
   *  @param tags Database column tags SqlType(VARCHAR), Length(200,true) */
  case class rReadingRecord(id: Long, cateId: Long = 0L, newsId: Long = 0L, newsTitle: String = "", userId: Long = 0L, createTime: Long = 0L, tags: String)
  /** GetResult implicit for fetching rReadingRecord objects using plain SQL queries */
  implicit def GetResultrReadingRecord(implicit e0: GR[Long], e1: GR[String]): GR[rReadingRecord] = GR{
    prs => import prs._
    rReadingRecord.tupled((<<[Long], <<[Long], <<[Long], <<[String], <<[Long], <<[Long], <<[String]))
  }
  /** Table description of table reading_record. Objects of this class serve as prototypes for rows in queries. */
  class tReadingRecord(_tableTag: Tag) extends Table[rReadingRecord](_tableTag, "reading_record") {
    def * = (id, cateId, newsId, newsTitle, userId, createTime, tags) <> (rReadingRecord.tupled, rReadingRecord.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(cateId), Rep.Some(newsId), Rep.Some(newsTitle), Rep.Some(userId), Rep.Some(createTime), Rep.Some(tags)).shaped.<>({r=>import r._; _1.map(_=> rReadingRecord.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column cate_id SqlType(BIGINT), Default(0) */
    val cateId: Rep[Long] = column[Long]("cate_id", O.Default(0L))
    /** Database column news_id SqlType(BIGINT), Default(0) */
    val newsId: Rep[Long] = column[Long]("news_id", O.Default(0L))
    /** Database column news_title SqlType(VARCHAR), Length(200,true), Default() */
    val newsTitle: Rep[String] = column[String]("news_title", O.Length(200,varying=true), O.Default(""))
    /** Database column user_id SqlType(BIGINT), Default(0) */
    val userId: Rep[Long] = column[Long]("user_id", O.Default(0L))
    /** Database column create_time SqlType(BIGINT), Default(0) */
    val createTime: Rep[Long] = column[Long]("create_time", O.Default(0L))
    /** Database column tags SqlType(VARCHAR), Length(200,true) */
    val tags: Rep[String] = column[String]("tags", O.Length(200,varying=true))
  }
  /** Collection-like TableQuery object for table tReadingRecord */
  lazy val tReadingRecord = new TableQuery(tag => new tReadingRecord(tag))

  /** Entity class storing rows of table tUser
   *  @param id Database column id SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param nickname Database column nickname SqlType(VARCHAR), Length(30,true), Default()
   *  @param mobile Database column mobile SqlType(VARCHAR), Length(20,true), Default()
   *  @param email Database column email SqlType(VARCHAR), Length(30,true), Default()
   *  @param username Database column username SqlType(VARCHAR), Length(30,true), Default()
   *  @param password Database column password SqlType(VARCHAR), Length(30,true), Default()
   *  @param sex Database column sex SqlType(VARCHAR), Length(6,true), Default()
   *  @param birthday Database column birthday SqlType(BIGINT), Default(0)
   *  @param pic Database column pic SqlType(VARCHAR), Length(100,true), Default()
   *  @param readNum Database column read_num SqlType(INT), Default(0)
   *  @param commentNum Database column comment_num SqlType(INT), Default(0)
   *  @param level Database column level SqlType(INT), Default(Some(0))
   *  @param createTime Database column create_time SqlType(BIGINT), Default(0)
   *  @param preference Database column preference SqlType(VARCHAR), Length(300,true), Default()
   *  @param token Database column token SqlType(VARCHAR), Length(300,true), Default()
   *  @param userType Database column user_type SqlType(INT), Default(0)
   *  @param signature Database column signature SqlType(VARCHAR), Length(200,true), Default() */
  case class rUser(id: Long, nickname: String = "", mobile: String = "", email: String = "", username: String = "", password: String = "", sex: String = "", birthday: Long = 0L, pic: String = "", readNum: Int = 0, commentNum: Int = 0, level: Option[Int] = Some(0), createTime: Long = 0L, preference: String = "", token: String = "", userType: Int = 0, signature: String = "")
  /** GetResult implicit for fetching rUser objects using plain SQL queries */
  implicit def GetResultrUser(implicit e0: GR[Long], e1: GR[String], e2: GR[Int], e3: GR[Option[Int]]): GR[rUser] = GR{
    prs => import prs._
    rUser.tupled((<<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[Long], <<[String], <<[Int], <<[Int], <<?[Int], <<[Long], <<[String], <<[String], <<[Int], <<[String]))
  }
  /** Table description of table user. Objects of this class serve as prototypes for rows in queries. */
  class tUser(_tableTag: Tag) extends Table[rUser](_tableTag, "user") {
    def * = (id, nickname, mobile, email, username, password, sex, birthday, pic, readNum, commentNum, level, createTime, preference, token, userType, signature) <> (rUser.tupled, rUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(nickname), Rep.Some(mobile), Rep.Some(email), Rep.Some(username), Rep.Some(password), Rep.Some(sex), Rep.Some(birthday), Rep.Some(pic), Rep.Some(readNum), Rep.Some(commentNum), level, Rep.Some(createTime), Rep.Some(preference), Rep.Some(token), Rep.Some(userType), Rep.Some(signature)).shaped.<>({r=>import r._; _1.map(_=> rUser.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12, _13.get, _14.get, _15.get, _16.get, _17.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("id", O.AutoInc, O.PrimaryKey)
    /** Database column nickname SqlType(VARCHAR), Length(30,true), Default() */
    val nickname: Rep[String] = column[String]("nickname", O.Length(30,varying=true), O.Default(""))
    /** Database column mobile SqlType(VARCHAR), Length(20,true), Default() */
    val mobile: Rep[String] = column[String]("mobile", O.Length(20,varying=true), O.Default(""))
    /** Database column email SqlType(VARCHAR), Length(30,true), Default() */
    val email: Rep[String] = column[String]("email", O.Length(30,varying=true), O.Default(""))
    /** Database column username SqlType(VARCHAR), Length(30,true), Default() */
    val username: Rep[String] = column[String]("username", O.Length(30,varying=true), O.Default(""))
    /** Database column password SqlType(VARCHAR), Length(30,true), Default() */
    val password: Rep[String] = column[String]("password", O.Length(30,varying=true), O.Default(""))
    /** Database column sex SqlType(VARCHAR), Length(6,true), Default() */
    val sex: Rep[String] = column[String]("sex", O.Length(6,varying=true), O.Default(""))
    /** Database column birthday SqlType(BIGINT), Default(0) */
    val birthday: Rep[Long] = column[Long]("birthday", O.Default(0L))
    /** Database column pic SqlType(VARCHAR), Length(100,true), Default() */
    val pic: Rep[String] = column[String]("pic", O.Length(100,varying=true), O.Default(""))
    /** Database column read_num SqlType(INT), Default(0) */
    val readNum: Rep[Int] = column[Int]("read_num", O.Default(0))
    /** Database column comment_num SqlType(INT), Default(0) */
    val commentNum: Rep[Int] = column[Int]("comment_num", O.Default(0))
    /** Database column level SqlType(INT), Default(Some(0)) */
    val level: Rep[Option[Int]] = column[Option[Int]]("level", O.Default(Some(0)))
    /** Database column create_time SqlType(BIGINT), Default(0) */
    val createTime: Rep[Long] = column[Long]("create_time", O.Default(0L))
    /** Database column preference SqlType(VARCHAR), Length(300,true), Default() */
    val preference: Rep[String] = column[String]("preference", O.Length(300,varying=true), O.Default(""))
    /** Database column token SqlType(VARCHAR), Length(300,true), Default() */
    val token: Rep[String] = column[String]("token", O.Length(300,varying=true), O.Default(""))
    /** Database column user_type SqlType(INT), Default(0) */
    val userType: Rep[Int] = column[Int]("user_type", O.Default(0))
    /** Database column signature SqlType(VARCHAR), Length(200,true), Default() */
    val signature: Rep[String] = column[String]("signature", O.Length(200,varying=true), O.Default(""))
  }
  /** Collection-like TableQuery object for table tUser */
  lazy val tUser = new TableQuery(tag => new tUser(tag))
}
