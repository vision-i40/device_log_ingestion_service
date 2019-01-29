package io_log_ingestion

import io_log_ingestion.devices.wise.{AdaptWiseLogToDeviceLog, IsWiseLog}
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime

case class DeviceLogRecord(
  deviceId: String,
  rawLog: String,
  deviceLog: Option[DeviceLog],
  detectedDevice: DeviceType.Value,
  receivedAt: DateTime,
  savedAt: DateTime = DateTime.now
)