package io_log_ingestion.devices

case object DeviceType extends Enumeration {
  val WISE: DeviceType.Value = Value("Wise")
  val UNKNOWN: DeviceType.Value = Value("Unknown")
}
