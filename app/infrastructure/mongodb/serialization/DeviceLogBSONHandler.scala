package infrastructure.mongodb.serialization

import io_log_ingestion.devices.DeviceLog
import reactivemongo.bson.{BSONDocumentHandler, Macros}

trait DeviceLogBSONHandler extends JodaTimeBSONHandler with DeviceTypeBSONHandler {
  implicit val deviceLogBSONHandler: BSONDocumentHandler[DeviceLog] = Macros.handler[DeviceLog]
}
