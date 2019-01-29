package io_log_ingestion

import com.google.inject.Singleton
import infrastructure.mongodb.Connection
import infrastructure.mongodb.serialization.DeviceLogRecordBSONHandler
import reactivemongo.api.collections.bson.BSONCollection
import concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class DeviceLogRecordRepository extends DeviceLogRecordBSONHandler {
  private val collectionName = "device_logs"

  def save(deviceLog: DeviceLogRecord): Future[DeviceLogRecord] = {
    collectionConnection.flatMap { collection =>
      collection
        .insert(deviceLog)
        .map(_ => deviceLog)
    }
  }

  def collectionConnection: Future[BSONCollection] = Connection().collection(collectionName)
}
