package io_log_ingestion

import infrastructure.IngestionEvent
import io_log_ingestion.devices.wise.{AdaptWiseLogToDeviceLog, IsWiseLog}
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime

case class DeviceLogRecord(
  deviceId: String,
  rawLog: String,
  deviceLog: Option[DeviceLog],
  detectedDevice: DeviceType.Value,
  receivedAt: DateTime,
  savedAt: DateTime = DateTime.now
)

//object DeviceLogRecord {
//  def apply(ingestionEvent: IngestionEvent): DeviceLogRecord = {
//    val deviceInfo = extractDeviceLogInfo(ingestionEvent.rawLog)
//    val deviceType = deviceInfo.map(_.deviceType).getOrElse(DeviceType.UNKNOWN)
//
//    DeviceLogRecord(
//      ingestionEvent.deviceId,
//      ingestionEvent.rawLog,
//      deviceInfo,
//      deviceType,
//      ingestionEvent.receivedAt
//    )
//  }
//
//  private def extractDeviceLogInfo(rawLog: String): Option[DeviceLog] = {
//    rawLog match {
//      case IsWiseLog(wiseLogInfo) => Some(AdaptWiseLogToDeviceLog(wiseLogInfo))
//      case _ => None
//    }
//  }
//}