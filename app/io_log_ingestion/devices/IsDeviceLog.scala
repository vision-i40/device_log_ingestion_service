package io_log_ingestion.devices

trait IsDeviceLog {
  def unapply(rawLog: String): Option[DeviceInfo]
}
