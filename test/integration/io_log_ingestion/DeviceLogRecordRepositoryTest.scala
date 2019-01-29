package integration.io_log_ingestion

import common.MongoDBHelper
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach, Matchers}

class DeviceLogRecordRepositoryTest extends AsyncFlatSpec with Matchers with BeforeAndAfterEach {

  override def beforeEach: Unit = {
    MongoDBHelper.reset
    MongoDBHelper.setupCollection()
  }

//  behavior of "IO Log Repository"
//  it should "save io log" in {
//    val expectedIoLog = IOLogBuilder().build
//
//    IOLogRepository.save(expectedIoLog).flatMap { _ =>
//      MongoDBHelper.getLast.map { maybeIOLog =>
//        maybeIOLog.isDefined shouldEqual true
//        val ioLog = maybeIOLog.get
//
//        ioLog.rawLog shouldEqual expectedIoLog.rawLog
//        ioLog.receivedAt shouldEqual expectedIoLog.receivedAt
//        ioLog.detectedDevice shouldEqual expectedIoLog.detectedDevice
//        ioLog.deviceLogInfo shouldEqual expectedIoLog.deviceLogInfo
//        ioLog.savedAt shouldEqual expectedIoLog.savedAt
//        ioLog.traceId shouldEqual expectedIoLog.traceId
//      }
//    }
//  }

}
