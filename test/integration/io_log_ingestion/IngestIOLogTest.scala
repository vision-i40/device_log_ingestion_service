package integration.io_log_ingestion

import common.MongoDBHelper
import common.builders.{IngestionEventBuilder, WiseJsonLogBuilder}
import io_log_ingestion.devices.DeviceType
import io_log_ingestion.{IOLogRepository, IngestIOLog}
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach, Matchers}

class IngestIOLogTest extends AsyncFlatSpec with Matchers with BeforeAndAfterEach {
  implicit private val repository: IOLogRepository = IOLogRepository

  override def beforeEach: Unit = {
    MongoDBHelper.reset
    MongoDBHelper.setupCollection()
  }

  behavior of "ingesting wise log"
  it should "save io log in database with wise device info" in {
    val firstInputValue = 0
    val secondInputValue = 571
    val wiseJsonBuilder = WiseJsonLogBuilder(Record = List(firstInputValue, secondInputValue))
    val wiseRawLog = wiseJsonBuilder.build
    val ingestionEvent = IngestionEventBuilder(rawLog = wiseRawLog).build

    val expectedChannelInputs = Map(
      "channel_1" -> firstInputValue.toString,
      "channel_2" -> secondInputValue.toString
    )

    IngestIOLog(ingestionEvent = ingestionEvent)
      .flatMap { _ =>
        MongoDBHelper.getLast.map { maybeIOLog =>
          maybeIOLog.isDefined shouldEqual true

          val actualIOLog = maybeIOLog.get

          actualIOLog.traceId shouldEqual ingestionEvent.traceId
          actualIOLog.deviceId shouldEqual ingestionEvent.deviceId
          actualIOLog.detectedDevice shouldEqual DeviceType.WISE
          actualIOLog.receivedAt shouldEqual ingestionEvent.receivedAt
          actualIOLog.rawLog shouldEqual wiseRawLog
          actualIOLog.deviceLogInfo.isDefined shouldEqual true

          val deviceLogInfo = actualIOLog.deviceLogInfo.get

          deviceLogInfo.deviceType shouldEqual DeviceType.WISE
          deviceLogInfo.uid shouldEqual wiseJsonBuilder.UID
          deviceLogInfo.logDateTime shouldEqual wiseJsonBuilder.TIM
          deviceLogInfo.channelInputs shouldEqual expectedChannelInputs
        }
      }
  }
}
