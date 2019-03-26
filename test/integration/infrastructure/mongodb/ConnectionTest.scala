package integration.infrastructure.mongodb

import common.MongoDBHelper
import common.builders.DeviceLogRecordBuilder
import infrastructure.config.MongoDBConfig
import infrastructure.mongodb.Connection
import infrastructure.mongodb.serialization.DeviceLogRecordBSONHandler
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

class ConnectionTest extends AsyncFlatSpec with Matchers with DeviceLogRecordBSONHandler {
  val config = MongoDBConfig()

  behavior of "Mongo DB Connection"
  it should "connect to database provided db and collection" in {
    val collectionName = "io_logs"
    val collectionFuture = Connection().collection(collectionName)

    collectionFuture.map { collection =>
      collection.name shouldEqual collectionName
      collection.db.name shouldEqual config.db
    }
  }

  it should "insert a document in collection" in {
    val collectionName = "device_logs"
    val collectionFuture = Connection().collection(collectionName)
    val deviceLog = DeviceLogRecordBuilder().build

    val futureDeviceLog = for {
      c <- collectionFuture
      _ <- c.insert(deviceLog)
      i <- MongoDBHelper.getByDeviceId(deviceLog.deviceId)
    } yield i

    futureDeviceLog.map { insertedDeviceLog =>
      insertedDeviceLog.isDefined shouldEqual true
      insertedDeviceLog.get shouldEqual deviceLog
    }
  }
}
