package common.builders

import java.util.UUID.randomUUID

import io_log_ingestion.IOLog
import io_log_ingestion.devices.{DeviceInfo, DeviceType}
import org.joda.time.DateTime

case class IOLogBuilder(
  traceId: String = randomUUID.toString,
  deviceId: String = randomUUID.toString,
  rawLog: String = "{'a': 'log'}",
  deviceLogInfo: Option[DeviceInfo] = Some(DeviceLogInfoBuilder().build),
  savedAt: DateTime = DateTime.now,
  detectedDevice : DeviceType.Value = DeviceType.UNKNOWN,
  receivedAt: DateTime = DateTime.now
) {
  def build: IOLog = {
    IOLog(traceId, deviceId, rawLog, deviceLogInfo,detectedDevice, receivedAt, savedAt)
  }
}
