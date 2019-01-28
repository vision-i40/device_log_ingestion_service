package io_log_ingestion.devices

import io_log_ingestion.devices.wise.WiseLog

trait IsDeviceLog {
  def unapply(rawLog: String): Option[WiseLog]
}
