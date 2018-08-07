package unit.io_log_ingestion

import java.time.LocalDateTime

import io_log_ingestion._
import org.mockito.Mockito
import org.mockito.Mockito.{when, _}
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterEach, Matchers}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

class IngestIOLogTest extends AsyncFlatSpec with Matchers with MockitoSugar with BeforeAndAfterEach {
  val expectedTraceId: String = "a-trace-id"
  val expectedLog: String = "a-log-event-plain-text"
  val expectedReceivedAt: LocalDateTime = LocalDateTime.now
  val ingestionEventJsonString =
    s"""
       |{
       |   "traceId": "$expectedTraceId",
       |   "log": "$expectedLog",
       |   "receivedAt": "${expectedReceivedAt.toString}"
       |}
       """.stripMargin

  val iOLogMock = mock[IOLog]
  val ingestionEventMock = mock[IngestionEvent]
  val parseIngestionEvent: ParseIngestionEvent = mock[ParseIngestionEvent]
  val storeIOLogMock: IOLogRepository = mock[IOLogRepository]


  override def beforeEach(): Unit = {
    Mockito.reset(parseIngestionEvent, storeIOLogMock)

    when(parseIngestionEvent.apply(ingestionEventJsonString)).thenReturn(Try(ingestionEventMock))
    when(storeIOLogMock.save(ingestionEventMock)).thenReturn(Future.successful(iOLogMock))
  }

  behavior of "ingest json log"
  it should "should call store IO Log in IO Log repository" in {
    val actualIOLog: Future[IOLog] = IngestIOLog(ingestionEventJsonString)(storeIOLogMock, parseIngestionEvent)

    actualIOLog.map{ ioLog: IOLog =>
      verify(parseIngestionEvent, times(1)).apply(ingestionEventJsonString)
      verify(storeIOLogMock, times(1)).save(ingestionEventMock)

      ioLog shouldEqual iOLogMock
    }
  }

}
