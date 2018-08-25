package unit.io_log_ingestion

import common.builders.{IngestionEventBuilder, WiseJsonLogBuilder}
import io_log_ingestion.IOLog
import io_log_ingestion.devices.DeviceType
import org.scalatest.{FlatSpec, Matchers}
class IOLogTest extends FlatSpec with Matchers {

  behavior of "create IO Log from unknown device ingestion event"
  it should "create an IO Log with unknown type and none device info" in {
    val ingestionEvent = IngestionEventBuilder().build

    val ioLog = IOLog(ingestionEvent)

    ioLog.traceId shouldEqual ingestionEvent.traceId
    ioLog.deviceId shouldEqual ingestionEvent.deviceId
    ioLog.receivedAt shouldEqual ingestionEvent.receivedAt
    ioLog.rawLog shouldEqual ingestionEvent.rawLog
    ioLog.deviceLogInfo shouldEqual None
    ioLog.detectedDevice shouldEqual DeviceType.UNKNOWN
  }

  behavior of "create IO Log from wise device ingestion event"
  it should "create an IO Log with Wise type and wise device info" in {
    val firstInputValue = 0
    val secondInputValue = 571
    val wiseJsonBuilder = WiseJsonLogBuilder(Record = List(firstInputValue, secondInputValue))
    val wiseRawLog = wiseJsonBuilder.build
    val ingestionEvent = IngestionEventBuilder(rawLog = wiseRawLog).build

    val expectedChannelInputs = Map(
      "channel_1" -> firstInputValue.toString,
      "channel_2" -> secondInputValue.toString
    )

    val ioLog = IOLog(ingestionEvent)

    ioLog.traceId shouldEqual ingestionEvent.traceId
    ioLog.deviceId shouldEqual ingestionEvent.deviceId
    ioLog.receivedAt shouldEqual ingestionEvent.receivedAt
    ioLog.rawLog shouldEqual ingestionEvent.rawLog
    ioLog.detectedDevice shouldEqual DeviceType.WISE
    ioLog.deviceLogInfo.isDefined shouldEqual true
    val deviceInfo = ioLog.deviceLogInfo.get

    deviceInfo.deviceType shouldEqual DeviceType.WISE
    deviceInfo.uid shouldEqual wiseJsonBuilder.UID
    deviceInfo.logDateTime shouldEqual wiseJsonBuilder.TIM
    deviceInfo.channelInputs shouldEqual expectedChannelInputs
  }

}
