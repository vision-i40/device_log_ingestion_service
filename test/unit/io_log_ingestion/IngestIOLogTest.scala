package unit.io_log_ingestion

import common.builders.IngestionEventBuilder
import infrastructure.IngestionEvent
import io_log_ingestion._
import org.joda.time.{DateTime, DateTimeZone}
import org.mockito.Matchers.any
import org.mockito.Mockito
import org.mockito.Mockito.{when, _}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import scala.concurrent.Future

class IngestIOLogTest extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfterEach {
  DateTimeZone.setDefault(DateTimeZone.UTC)

  val ingestionEvent: IngestionEvent = IngestionEventBuilder().build
  val iOLogMock: IOLog = mock[IOLog]
  val ingestionEventMock: IngestionEvent = mock[IngestionEvent]
  val ioLogRepository: IOLogRepository = mock[IOLogRepository]

  override def beforeEach(): Unit = {
    Mockito.reset(ioLogRepository)

    when(ioLogRepository.save(any[IOLog])).thenReturn(Future.successful(iOLogMock))
  }

  behavior of "ingest event"
  it should "call io log repository" in {
    IngestIOLog(ingestionEvent)(ioLogRepository)

    eventually {
      verify(ioLogRepository, times(1)).save(any[IOLog])
    }
  }

}
