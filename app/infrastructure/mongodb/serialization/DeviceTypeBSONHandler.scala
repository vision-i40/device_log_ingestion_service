package infrastructure.mongodb.serialization

import io_log_ingestion.devices.DeviceType
import reactivemongo.bson.{BSONHandler, BSONString}

trait DeviceTypeBSONHandler {
  implicit object DeviceTypeBSONHandler extends BSONHandler[BSONString, DeviceType.Value] {
    def read(deviceType: BSONString): DeviceType.Value = DeviceType.withName(deviceType.value)

    def write(deviceType: DeviceType.Value) = BSONString(deviceType.toString)
  }
}
