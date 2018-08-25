package io_log_ingestion.devices

import org.joda.time.DateTime

case class DeviceInfo(
  deviceType: DeviceType.Value,
  uid: String,
  logDateTime: DateTime,
  channelInputs: Map[String, String]
)
