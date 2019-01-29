package unit.io_log_ingestion

import common.builders.DeviceLogPayloadBuilder
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import io_log_ingestion.{DeviceLogRecord, LogIngestionService}
import org.joda.time.DateTime
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach, Matchers}

import scala.concurrent.duration._
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

  it should "set device log with extract wise log data" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    val actualDeviceLogRecord: Future[DeviceLogRecord] = service.ingest(wiseLog)

    actualDeviceLogRecord.map { deviceLogRecord =>
      deviceLogRecord.deviceLog.isDefined shouldEqual true
      deviceLogRecord.deviceLog.get shouldEqual DeviceLog(
        DeviceType.WISE,
        deviceLogPayloadBuilder.uid,
        deviceLogPayloadBuilder.tim,
        Map(
          "channel_1" -> "1",
          "channel_2" -> "3",
          "channel_3" -> "2",
          "channel_4" -> "5",
          "channel_5" -> "6"
        )
      )
    }
  }

  it should "generate different device id over the time" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    val futureDeviceLogRecords: Future[(DeviceLogRecord, DeviceLogRecord, DeviceLogRecord)] = for {
      dlOne <- service.ingest(wiseLog)
      dlTwo <- service.ingest(wiseLog)
      dlThree <- service.ingest(wiseLog)
    } yield (dlOne, dlTwo, dlThree)

    futureDeviceLogRecords.map { deviceLogRecords =>
      deviceLogRecords._1.deviceId shouldNot equal(deviceLogRecords._2.deviceId)
      deviceLogRecords._2.deviceId shouldNot equal(deviceLogRecords._3.deviceId)
      deviceLogRecords._3.deviceId shouldNot equal(deviceLogRecords._1.deviceId)
    }
  }

  it should "set savedAt at least a second ago" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    val actualDeviceLogRecord: Future[DeviceLogRecord] = service.ingest(wiseLog)

    actualDeviceLogRecord.map { deviceLogRecord =>
      deviceLogRecord.savedAt.plusSeconds(1).isAfterNow shouldEqual true
    }
  }

  behavior of "ingestion of unknown device log"
  it should "detect device type as unknown" in {
    val unknownLog = "unrecognized-log-type"

    val actualDeviceLogRecord: Future[DeviceLogRecord] = service.ingest(unknownLog)

    actualDeviceLogRecord.map { deviceLogRecord =>
      deviceLogRecord.detectedDevice shouldEqual DeviceType.UNKNOWN
    }
  }

  it should "it should leave device log as None" in {
    val unknownLog = "unrecognized-log-type"

    val actualDeviceLogRecord: Future[DeviceLogRecord] = service.ingest(unknownLog)

    actualDeviceLogRecord.map { deviceLogRecord =>
      deviceLogRecord.deviceLog shouldEqual None
    }
  }
}
