package infrastructure.parsing

import io_log_ingestion.devices.DeviceType
import play.api.libs.json._

trait DeviceTypeReadersAndWriters {
  implicit val implicitDeviceTypeReaders: Reads[DeviceType.Value] = Reads.enumNameReads(DeviceType)
  implicit val implicitDeviceTypeWriters: Writes[DeviceType.Value] = Writes.enumNameWrites
}
