package unit.io_log_ingestion

import common.builders.DeviceLogPayloadBuilder
import io_log_ingestion.devices.DeviceType
import io_log_ingestion.{DeviceLogRecord, LogIngestionService}
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach, Matchers}

import scala.concurrent.Future

class LogIngestionServiceTest extends AsyncFlatSpec with Matchers with BeforeAndAfterEach {
  val service = new LogIngestionService()

  behavior of "ingestion of a wise log"
  it should "build device log record with proper rawLog" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    val actualDeviceLogRecord: Future[DeviceLogRecord] = service.ingest(wiseLog)

    actualDeviceLogRecord.map { deviceLogRecord =>
      deviceLogRecord.rawLog shouldEqual wiseLog
    }
  }

  it should "detect device type as wise" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    val actualDeviceLogRecord: Future[DeviceLogRecord] = service.ingest(wiseLog)

    actualDeviceLogRecord.map { deviceLogRecord =>
      deviceLogRecord.detectedDevice shouldEqual DeviceType.WISE
    }
  }


}
