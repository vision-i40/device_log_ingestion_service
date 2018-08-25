package infrastructure.mongodb.serialization

import io_log_ingestion.IOLog
import reactivemongo.bson.{BSONDocumentHandler, Macros}

trait IOLogBSONHandler extends DeviceTypeBSONHandler with JodaTimeBSONHandler with DeviceLogInfoBSONHandler {
  implicit val ioLogBSONHandler: BSONDocumentHandler[IOLog] = Macros.handler[IOLog]
}
