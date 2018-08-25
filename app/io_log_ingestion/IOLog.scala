package io_log_ingestion

import infrastructure.IngestionEvent
import io_log_ingestion.devices.wise.IsWiseLog
import io_log_ingestion.devices.{DeviceInfo, DeviceType}
import org.joda.time.DateTime

case class IOLog(
  traceId: String,
  deviceId: String,
  rawLog: String,
  deviceLogInfo: Option[DeviceInfo],
  detectedDevice: DeviceType.Value,
  receivedAt: DateTime,
  savedAt: DateTime = DateTime.now
)

object IOLog {
  def apply(ingestionEvent: IngestionEvent): IOLog = {
    val deviceInfo = extractDeviceLogInfo(ingestionEvent.rawLog)
    val deviceType = deviceInfo.map(_.deviceType).getOrElse(DeviceType.UNKNOWN)

    new IOLog(
      ingestionEvent.traceId,
      ingestionEvent.deviceId,
      ingestionEvent.rawLog,
      deviceInfo,
      deviceType,
      ingestionEvent.receivedAt
    )
  }

  private def extractDeviceLogInfo(rawLog: String): Option[DeviceInfo] = {
    rawLog match {
      case IsWiseLog(wiseLogInfo) => Some(wiseLogInfo)
      case _ => None
    }
  }
}