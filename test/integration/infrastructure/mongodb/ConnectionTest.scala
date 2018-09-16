package integration.infrastructure.mongodb

import common.MongoDBHelper
import common.builders.IOLogBuilder
import config.MongoDBConfig
import infrastructure.mongodb.Connection
import infrastructure.mongodb.serialization.IOLogBSONHandler
import org.scalatest.{AsyncFlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

class ConnectionTest extends AsyncFlatSpec with Matchers with IOLogBSONHandler {
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
    val collectionName = "io_logs"
    val collectionFuture = Connection().collection(collectionName)
    val ioLog = IOLogBuilder().build

    val futureIOLog = for {
      c <- collectionFuture
      _ <- c.insert(ioLog)
      i <- MongoDBHelper.getByDeviceId(ioLog.deviceId)
    } yield i

    futureIOLog.map { insertedIOLog =>
      insertedIOLog.isDefined shouldEqual true
      insertedIOLog.get shouldEqual ioLog
    }
  }
}
