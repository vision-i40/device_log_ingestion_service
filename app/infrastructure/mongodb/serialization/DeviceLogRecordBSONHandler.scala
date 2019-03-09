package infrastructure.mongodb.serialization

import io_log_ingestion.DeviceLogRecord
import reactivemongo.bson.{BSONDocument, BSONDocumentHandler, Macros}

trait DeviceLogRecordBSONHandler extends DeviceTypeBSONHandler with JodaTimeBSONHandler with DeviceLogBSONHandler {
  implicit val deviceLogRecordBSONHandler: BSONDocumentHandler[DeviceLogRecord] = Macros.handler[DeviceLogRecord]

  val deviceLogsProjection = Some(BSONDocument(
    "deviceId" -> 1,
    "rawLog" -> 1,
    "deviceLog" -> 1,
    "detectedDevice" -> 1,
    "savedAt" -> 1
  ))
}
