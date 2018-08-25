package io_log_ingestion.devices.wise

import io_log_ingestion.devices.{DeviceInfo, DeviceType, IsDeviceLog}

object IsWiseLog extends IsDeviceLog {
  def unapply(rawLog: String): Option[DeviceInfo] = {
    ParseWiseLog(rawLog)
      .map(adaptToDeviceLogInfo)
      .toOption
  }

  private def adaptToDeviceLogInfo(wiseLog: WiseLog): DeviceInfo = {
    DeviceInfo(
      uid = wiseLog.UID,
      deviceType = DeviceType.WISE,
      logDateTime = wiseLog.TIM,
      channelInputs = mapChannelInputs(wiseLog.Record)
    )
  }

  private def mapChannelInputs(records: List[Int]): Map[String, String] = {
    records.zipWithIndex.map { recordAndIndex =>
      (s"channel_${recordAndIndex._2 + 1}", recordAndIndex._1.toString)
    }.toMap
  }
}
