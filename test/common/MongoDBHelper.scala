package common

import config.MongoDBConfig
import infrastructure.mongodb.serialization.IOLogBSONHandler
import io_log_ingestion.IOLog
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.BSONDocument
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}

object MongoDBHelper extends IOLogBSONHandler {
  val collectionName = "io_logs"

  private val config = MongoDBConfig()
  private val driver = MongoDriver()
  private val parsedUri = MongoConnection.parseURI(s"mongodb://${config.username}:${config.password}@${config.host}:${config.port}/${config.db}")
  private val db: Future[DefaultDB] = Future.fromTry(parsedUri.map(driver.connection)).flatMap(_.database(config.db))
  private val collectionFuture = db.map((db: DefaultDB) => db[BSONCollection](collectionName))

  def reset: Boolean = {
    Await.result(collectionFuture.flatMap(_.drop(false)), 10 seconds)
  }

  def setupCollection: Unit = {
    Await.result(collectionFuture.flatMap(_.create), 10 seconds)
  }

  def getLast: Future[Option[IOLog]] = {
    collectionFuture.flatMap { collection =>
      collection
        .find(BSONDocument())
        .sort(BSONDocument("_id" -> -1))
        .one[IOLog]
    }

  }
}
