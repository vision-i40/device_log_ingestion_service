package unit.io_log_ingestion

import common.builders.DeviceLogPayloadBuilder
import io_log_ingestion.devices.{DeviceLog, DeviceType}
import io_log_ingestion.{DeviceLogRecord, DeviceLogRecordRepository, LogIngestionService}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.mockito.{ArgumentMatcher, Mockito, Matchers => M}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LogIngestionServiceTest extends FlatSpec with MockitoSugar with Matchers with BeforeAndAfterEach {
  private val repository = mock[DeviceLogRecordRepository]
  private val service = new LogIngestionService(repository)

  override def beforeEach(): Unit = {
    super.beforeEach()
    Mockito.reset(repository)
    when(repository.save(any[DeviceLogRecord])).thenReturn(Future { mock[DeviceLogRecord] } )
  }

  behavior of "ingestion of a wise log"
  it should "build device log record with proper rawLog" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    service.ingest(wiseLog)

    eventually {
      verify(repository, times(1)).save(M.argThat(HasRawLog(wiseLog)))
    }
  }

  it should "detect device type as wise" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    service.ingest(wiseLog)

    eventually {
      verify(repository, times(1)).save(M.argThat(DetectDevice(DeviceType.WISE)))
    }
  }

  it should "set device log with extract wise log data" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    service.ingest(wiseLog)

    eventually {
      val someDeviceLog = Some(DeviceLog(
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
      ))

      verify(repository, times(1)).save(M.argThat(ExtractDeviceLog(someDeviceLog)))
    }
  }

  it should "set savedAt at least one second ago" in {
    val deviceLogPayloadBuilder = DeviceLogPayloadBuilder()
    val wiseLog = deviceLogPayloadBuilder.build

    service.ingest(wiseLog)

    eventually {
      verify(repository, times(1)).save(M.argThat(SaveAtLeastOneSecondAgo()))
    }
  }

  behavior of "ingestion of unknown device log"
  it should "detect device type as unknown" in {
    val unknownLog = "unrecognized-log-type"

    service.ingest(unknownLog)

    eventually {
      verify(repository, times(1)).save(M.argThat(DetectDevice(DeviceType.UNKNOWN)))
    }
  }

  it should "it should leave device log as None" in {
    val unknownLog = "unrecognized-log-type"

    service.ingest(unknownLog)

    eventually {
      verify(repository, times(1)).save(M.argThat(ExtractDeviceLog(None)))
    }
  }

  case class HasRawLog(rawLog: String) extends ArgumentMatcher[DeviceLogRecord] {
    def matches(request: Any): Boolean =
      request match {
        case a: DeviceLogRecord if a.rawLog == rawLog => true
        case _ => false
      }
  }

  case class DetectDevice(deviceType: DeviceType.Value) extends ArgumentMatcher[DeviceLogRecord] {
    def matches(request: Any): Boolean =
      request match {
        case a: DeviceLogRecord if a.detectedDevice == deviceType => true
        case _ => false
      }
  }

  case class ExtractDeviceLog(someDeviceLog: Option[DeviceLog]) extends ArgumentMatcher[DeviceLogRecord] {
    def matches(request: Any): Boolean =
      request match {
        case a: DeviceLogRecord if a.deviceLog == someDeviceLog => true
        case _ => false
      }
  }

  case class SaveAtLeastOneSecondAgo() extends ArgumentMatcher[DeviceLogRecord] {
    def matches(request: Any): Boolean =
      request match {
        case a: DeviceLogRecord if a.savedAt.plusSeconds(1).isAfterNow => true
        case _ => false
      }
  }
}
