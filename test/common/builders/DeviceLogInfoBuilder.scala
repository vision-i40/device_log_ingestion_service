package common.builders

import java.util.UUID.randomUUID
import io_log_ingestion.devices.{DeviceInfo, DeviceType}
import org.joda.time.DateTime

case class DeviceLogInfoBuilder(
  uid: String = randomUUID.toString,
  deviceType: DeviceType.Value = DeviceType.UNKNOWN,
  logDateTime: DateTime = DateTime.now,
  channelInputs: Map[String, String] = Map(
    "channel_1" -> "123",
    "channel_2" -> "456"
  )
 ) {
  def build: DeviceInfo = {
   DeviceInfo(
     deviceType = deviceType,
     uid = uid,
     logDateTime = logDateTime,
     channelInputs = channelInputs
   )
  }
}
