package unit.infrastructure.parsing

import infrastructure.IngestionEvent
import infrastructure.parsing.ParseIngestionEvent
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.{FlatSpec, Matchers}
import scala.util.Try

class IngestionEventTest extends FlatSpec with Matchers {
  DateTimeZone.setDefault(DateTimeZone.UTC)

  val expectedTraceId: String = "a-trace-id"
  val expectedDeviceId: String = "a-device-id"
  val expectedLog: String = "a-log-event-plain-text"
  val expectedReceivedAt: DateTime = new DateTime(DateTime.now.getMillis)

  behavior of "valid ingestion event"
  it should "properly parse valid ingestion event" in {
    val ingestionEventJsonString =
      s"""
         |{
         |   "traceId": "$expectedTraceId",
         |   "deviceId": "$expectedDeviceId",
         |   "rawLog": "$expectedLog",
         |   "receivedAt": "${expectedReceivedAt.toString}"
         |}
       """.stripMargin

    val tryActualIngestionEvent: Try[IngestionEvent] = ParseIngestionEvent(ingestionEventJsonString)

    tryActualIngestionEvent.isSuccess shouldEqual true

    val actualIngestionEvent = tryActualIngestionEvent.get

    actualIngestionEvent.traceId shouldEqual expectedTraceId
    actualIngestionEvent.deviceId shouldEqual expectedDeviceId
    actualIngestionEvent.rawLog shouldEqual expectedLog
    actualIngestionEvent.receivedAt shouldEqual expectedReceivedAt
  }

  behavior of "invalid ingestion event"
  it should "fail with event without traceId field" in {
    val ingestionEventJsonString =
      s"""
         |{
         |   "rawLog": "$expectedLog",
         |   "receivedAt": "${expectedReceivedAt.toString}"
         |}
       """.stripMargin

    val tryActualIngestionEvent: Try[IngestionEvent] = ParseIngestionEvent(ingestionEventJsonString)

    tryActualIngestionEvent.isFailure shouldEqual true
  }

  it should "fail with event without log field" in {
    val ingestionEventJsonString =
      s"""
         |{
         |  "traceId": "$expectedTraceId",
         |  "receivedAt": "${expectedReceivedAt.toString}"
         |}
       """.stripMargin

    val tryActualIngestionEvent: Try[IngestionEvent] = ParseIngestionEvent(ingestionEventJsonString)

    tryActualIngestionEvent.isFailure shouldEqual true
  }

  it should "fail with event without receivedAt field" in {
    val ingestionEventJsonString =
      s"""
         |{
         |  "traceId": "$expectedTraceId",
         |  "rawLog": "$expectedLog",
         |}
       """.stripMargin

    val tryActualIngestionEvent: Try[IngestionEvent] = ParseIngestionEvent(ingestionEventJsonString)

    tryActualIngestionEvent.isFailure shouldEqual true
  }

}
