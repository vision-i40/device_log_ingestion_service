package io_log_ingestion

import infrastructure.mongodb.Connection
import infrastructure.mongodb.serialization.IOLogBSONHandler
import reactivemongo.api.collections.bson.BSONCollection
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait IOLogRepository extends IOLogBSONHandler {
  private val collectionName = "io_logs"

  def save(ioLog: IOLog): Future[IOLog] = {
    collectionConnection.flatMap { collection =>
      collection
        .insert(ioLog)
        .map(_ => ioLog)
    }
  }

  def collectionConnection: Future[BSONCollection] = Connection().collection(collectionName)
}

object IOLogRepository extends IOLogRepository