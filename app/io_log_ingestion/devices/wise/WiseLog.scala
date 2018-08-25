package io_log_ingestion.devices.wise

import org.joda.time.DateTime

case class WiseLog(
  PE: String,
  UID: String,
  MAC: String,
  TIM: DateTime,
  Record: List[Int]
)
