package infrastructure.parsing

import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait DeviceLogReadersAndWriters extends JodaDateTimeReadersAndWriters with DeviceTypeReadersAndWriters  {
  implicit val implicitDeviceLogWriters: Writes[DeviceLog] = (
    (__ \ "deviceType").write[DeviceType.Value] and
    (__ \ "uid").write[String] and
    (__ \ "logDateTime").write[DateTime] and
    (__ \ "channelInputs").write[Map[String, String]]
    )(unlift(DeviceLog.unapply))

  implicit def implicitDeviceLogReaders: Reads[DeviceLog] = (
    (__ \ "deviceType").read[DeviceType.Value] and
    (__ \ "uid").read[String] and
    (__ \ "logDateTime").read[DateTime] and
    (__ \ "channelInputs").read[Map[String, String]]
    )(DeviceLog.apply _)
}
