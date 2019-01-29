package io_log_ingestion

import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime

case class DeviceLogRecord(
  deviceId: String,
  rawLog: String,
  deviceLog: Option[DeviceLog],
  detectedDevice: DeviceType.Value,
  savedAt: DateTime = DateTime.now
)