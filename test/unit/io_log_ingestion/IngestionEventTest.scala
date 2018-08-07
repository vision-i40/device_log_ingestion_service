package unit.io_log_ingestion

import java.time.LocalDateTime
import io_log_ingestion.{IngestionEvent, ParseIngestionEvent}
import org.scalatest.{FlatSpec, Matchers}
import scala.util.Try

class IngestionEventTest extends FlatSpec with Matchers {
  val expectedTraceId: String = "a-trace-id"
  val expectedLog: String = "a-log-event-plain-text"
  val expectedReceivedAt: LocalDateTime = LocalDateTime.now

  behavior of "valid ingestion event"
  it should "properly parse valid ingestion event" in {
    val ingestionEventJsonString =
      s"""
         |{
         |   "traceId": "$expectedTraceId",
         |   "log": "$expectedLog",
         |   "receivedAt": "${expectedReceivedAt.toString}"
         |}
       """.stripMargin

    val tryActualIngestionEvent: Try[IngestionEvent] = ParseIngestionEvent(ingestionEventJsonString)

    tryActualIngestionEvent.isSuccess shouldEqual true

    val actualIngestionEvent = tryActualIngestionEvent.get

    actualIngestionEvent.traceId shouldEqual expectedTraceId
    actualIngestionEvent.log shouldEqual expectedLog
    actualIngestionEvent.receivedAt shouldEqual expectedReceivedAt
  }

  behavior of "invalid ingestion event"
  it should "fail with event without traceId field" in {
    val ingestionEventJsonString =
      s"""
         |{
         |   "log": "$expectedLog",
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
         |  "log": "$expectedLog",
         |}
       """.stripMargin

    val tryActualIngestionEvent: Try[IngestionEvent] = ParseIngestionEvent(ingestionEventJsonString)

    tryActualIngestionEvent.isFailure shouldEqual true
  }

}
