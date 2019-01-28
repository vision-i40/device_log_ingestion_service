package common.builders

import java.util.UUID.randomUUID
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime

case class DeviceLogBuilder(
  uid: String = randomUUID.toString,
  deviceType: DeviceType.Value = DeviceType.UNKNOWN,
  logDateTime: DateTime = DateTime.now,
  channelInputs: Map[String, String] = Map(
    "channel_1" -> "123",
    "channel_2" -> "456"
  )
 ) {
  def build: DeviceLog = {
   DeviceLog(
     deviceType = deviceType,
     uid = uid,
     logDateTime = logDateTime,
     channelInputs = channelInputs
   )
  }
}
