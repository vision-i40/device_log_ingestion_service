package common

import config.MongoDBConfig
import infrastructure.mongodb.serialization.IOLogBSONHandler
import io_log_ingestion.IOLog
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}
import reactivemongo.bson.BSONDocument
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

object MongoDBHelper extends IOLogBSONHandler {
  private val collectionName = "io_logs"
  private val config = MongoDBConfig()
  private val driver = MongoDriver()
  private val parsedUri = MongoConnection.parseURI(config.uri)
  private val collectionConnection = getCollectionConnection

  private val projection = Some(BSONDocument(
    "traceId" -> 1,
    "deviceId" -> 1,
    "rawLog" -> 1,
    "deviceLogInfo" -> 1,
    "detectedDevice" -> 1,
    "receivedAt" -> 1,
    "savedAt" -> 1
  ))

  def reset: Boolean = {
    Await.result(collectionConnection.flatMap(_.drop(failIfNotFound = false)), 10 seconds)
  }

  def setupCollection(): Unit = {
    Await.result(collectionConnection.flatMap(_.create), 10 seconds)
  }

  def getLast: Future[Option[IOLog]] = {
    collectionConnection.flatMap { collection =>
      collection
        .find(BSONDocument(), projection)
        .sort(BSONDocument("_id" -> -1))
        .one[IOLog]
    }
  }

  def getByDeviceId(deviceId: String): Future[Option[IOLog]] = {
    collectionConnection.flatMap { collection =>
      collection
        .find(BSONDocument("deviceId" -> deviceId), projection)
        .sort(BSONDocument("_id" -> -1))
        .one[IOLog]
    }
  }

  private def getCollectionConnection: Future[BSONCollection] = {
    Future.fromTry(parsedUri.map(driver.connection))
      .flatMap(_.database(config.db))
      .map((db: DefaultDB) => db[BSONCollection](collectionName))
  }
}
