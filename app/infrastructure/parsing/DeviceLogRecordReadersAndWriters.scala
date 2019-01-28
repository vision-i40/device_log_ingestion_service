package infrastructure.parsing

import io_log_ingestion.DeviceLogRecord
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait DeviceLogRecordReadersAndWriters extends JodaDateTimeReadersAndWriters with DeviceLogReadersAndWriters
  with DeviceTypeReadersAndWriters {

  implicit def implicitDeviceLogRecordWriters: Writes[DeviceLogRecord] = (
    (__ \ "deviceId").write[String] and
    (__ \ "rawLog").write[String] and
    (__ \ "deviceLog").writeNullable[DeviceLog] and
    (__ \ "detectedDevice").write[DeviceType.Value] and
    (__ \ "receivedAt").write[DateTime] and
    (__ \ "savedAt").write[DateTime]
  )(unlift(DeviceLogRecord.unapply))

  implicit def implicitDeviceLogRecordReaders: Reads[DeviceLogRecord] = (
    (__ \ "deviceId").read[String] and
    (__ \ "rawLog").read[String] and
    (__ \ "deviceLog").readNullable[DeviceLog] and
    (__ \ "detectedDevice").read[DeviceType.Value] and
    (__ \ "receivedAt").read[DateTime] and
    (__ \ "savedAt").read[DateTime]
    )(DeviceLogRecord.apply _)
}
