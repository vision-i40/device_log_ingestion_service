package io_log_ingestion

import com.google.inject.Singleton
import io_log_ingestion.devices.DeviceType
import org.joda.time.DateTime
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class LogIngestionService {
  def ingest(deviceLog: String): Future[DeviceLogRecord] = {
    Future {
      DeviceLogRecord(
        deviceId = "blabla",
        rawLog = deviceLog,
        deviceLog = None,
        detectedDevice = DeviceType.WISE,
        receivedAt = DateTime.now,
        savedAt = DateTime.now
      )
    }
  }
}

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