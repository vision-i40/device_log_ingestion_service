package common.builders

import io_log_ingestion.DeviceLogRecord
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime

case class DeviceLogRecordBuilder(
   deviceId: String = "a-deviceID",
   rawLog: String = "a-raw-log",
   deviceLog: Option[DeviceLog] = Some(DeviceLogBuilder().build),
   detectedDevice: DeviceType.Value = DeviceType.UNKNOWN,
   receivedAt: DateTime = DateTime.now(),
   savedAt: DateTime = DateTime.now()) {

  def build: DeviceLogRecord = DeviceLogRecord(
    deviceId = deviceId,
    rawLog = rawLog,
    deviceLog = deviceLog,
    detectedDevice = detectedDevice,
    savedAt = savedAt
  )
}
