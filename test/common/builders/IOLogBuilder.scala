package common.builders

import java.util.UUID.randomUUID

import io_log_ingestion.DeviceLogRecord
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime

case class IOLogBuilder(
                         traceId: String = randomUUID.toString,
                         deviceId: String = randomUUID.toString,
                         rawLog: String = "{'a': 'log'}",
                         deviceLogInfo: Option[DeviceLog] = Some(DeviceLogBuilder().build),
                         savedAt: DateTime = DateTime.now,
                         detectedDevice : DeviceType.Value = DeviceType.UNKNOWN,
                         receivedAt: DateTime = DateTime.now
) {
  def build: DeviceLogRecord = {
    DeviceLogRecord(deviceId, rawLog, deviceLogInfo,detectedDevice, receivedAt, savedAt)
  }
}
