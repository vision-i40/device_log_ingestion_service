package io_log_ingestion.devices.wise

import io_log_ingestion.devices.{DeviceLog, DeviceType, IsDeviceLog}

object IsWiseLog extends IsDeviceLog {
  def unapply(rawLog: String): Option[WiseLog] = {
    ParseWiseLog(rawLog).toOption
  }
}
