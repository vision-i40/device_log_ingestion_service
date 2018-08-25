package infrastructure.mongodb.serialization

import io_log_ingestion.devices.DeviceInfo
import reactivemongo.bson.{BSONDocumentHandler, Macros}

trait DeviceLogInfoBSONHandler extends JodaTimeBSONHandler with DeviceTypeBSONHandler {
  implicit val deviceLogInfoBSONHandler: BSONDocumentHandler[DeviceInfo] = Macros.handler[DeviceInfo]
}
