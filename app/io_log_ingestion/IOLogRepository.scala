package io_log_ingestion

import infrastructure.mongodb.Connection
import infrastructure.mongodb.serialization.IOLogBSONHandler
import reactivemongo.api.collections.bson.BSONCollection
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait IOLogRepository extends IOLogBSONHandler {
  val collectionName = "io_logs"
  private val ioLogCollection: Future[BSONCollection] = Connection().collection(collectionName)

  def save(ioLog: IOLog): Future[IOLog] = {
    ioLogCollection.flatMap { collection =>
      collection
        .insert(ioLog)
        .map(_ => ioLog)
    }
  }
}

object IOLogRepository extends IOLogRepository