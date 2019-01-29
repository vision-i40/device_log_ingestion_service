package io_log_ingestion.devices.wise

import io_log_ingestion.devices.IsDeviceLog

object IsWiseLog extends IsDeviceLog {
  def unapply(rawDeviceLog: String): Option[WiseLog] = {
    ParseWiseLog(rawDeviceLog).toOption
  }
}
