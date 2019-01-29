package integration.io_log_ingestion

import common.MongoDBHelper
import common.builders.DeviceLogRecordBuilder
import io_log_ingestion.DeviceLogRecordRepository
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach, Matchers}

class DeviceLogRecordRepositoryTest extends AsyncFlatSpec with Matchers with BeforeAndAfterEach {

  val repository = new DeviceLogRecordRepository()

  override def beforeEach: Unit = {
    MongoDBHelper.reset
    MongoDBHelper.setupCollection()
  }

  behavior of "Device Log Repository"
  it should "save device log" in {
    val expectedDeviceLog = DeviceLogRecordBuilder().build

    repository.save(expectedDeviceLog).flatMap { _ =>
      MongoDBHelper.getLast.map { maybeDeviceLog =>
        maybeDeviceLog.isDefined shouldEqual true
        val deviceLog = maybeDeviceLog.get

        deviceLog.rawLog shouldEqual expectedDeviceLog.rawLog
        deviceLog.detectedDevice shouldEqual expectedDeviceLog.detectedDevice
        deviceLog.deviceLog shouldEqual expectedDeviceLog.deviceLog
        deviceLog.savedAt shouldEqual expectedDeviceLog.savedAt
      }
    }
  }

}
