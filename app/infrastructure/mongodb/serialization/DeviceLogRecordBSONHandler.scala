package infrastructure.mongodb.serialization

import io_log_ingestion.DeviceLogRecord
import reactivemongo.bson.{BSONDocumentHandler, Macros}

trait DeviceLogRecordBSONHandler extends DeviceTypeBSONHandler with JodaTimeBSONHandler with DeviceLogBSONHandler {
  implicit val deviceLogRecordBSONHandler: BSONDocumentHandler[DeviceLogRecord] = Macros.handler[DeviceLogRecord]
}
