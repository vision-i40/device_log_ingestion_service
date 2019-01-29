package io_log_ingestion

import infrastructure.mongodb.Connection
import infrastructure.mongodb.serialization.DeviceLogRecordBSONHandler
import reactivemongo.api.collections.bson.BSONCollection
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait DeviceLogRecordRepository extends DeviceLogRecordBSONHandler {
  private val collectionName = "io_logs"

  def save(ioLog: DeviceLogRecord): Future[DeviceLogRecord] = {
    collectionConnection.flatMap { collection =>
      collection
        .insert(ioLog)
        .map(_ => ioLog)
    }
  }

  def collectionConnection: Future[BSONCollection] = Connection().collection(collectionName)
}

object DeviceLogRecordRepository extends DeviceLogRecordRepository