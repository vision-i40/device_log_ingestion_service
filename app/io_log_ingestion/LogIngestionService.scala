package io_log_ingestion

import java.util.UUID.randomUUID

import com.google.inject.Singleton
import io_log_ingestion.devices.wise.{AdaptWiseLogToDeviceLog, IsWiseLog}
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import org.joda.time.DateTime

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class LogIngestionService {
  def ingest(rawDeviceLog: String): Future[DeviceLogRecord] = {
    val deviceLog = extractDeviceLog(rawDeviceLog)

    val deviceLogRecord = DeviceLogRecord(
      deviceId = randomUUID().toString,
      rawLog = rawDeviceLog,
      deviceLog = deviceLog,
      detectedDevice = deviceLog.map(_.deviceType).getOrElse(DeviceType.UNKNOWN),
      savedAt = DateTime.now
    )

    Future { deviceLogRecord }
  }

  def extractDeviceLog(rawDeviceLog: String): Option[DeviceLog] = rawDeviceLog match {
    case IsWiseLog(wiseLog) => Some(AdaptWiseLogToDeviceLog(wiseLog))
    case _ => None
  }
}