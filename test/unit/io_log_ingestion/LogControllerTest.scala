package unit.io_log_ingestion

import common.builders.{DeviceLogPayloadBuilder, DeviceLogRecordBuilder}
import io_log_ingestion.{DeviceLogRecord, LogController, LogIngestionService}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, Matchers}
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LogControllerTest extends FlatSpec with Matchers with MockitoSugar {
  val service: LogIngestionService = mock[LogIngestionService]
  val controller = new LogController(service)

  behavior of "receiving a wise log"
  it should "successfully handle the request" in {
    val payloadBuilder = DeviceLogPayloadBuilder()
    val payload = payloadBuilder.build
    val deviceLogRecord = DeviceLogRecordBuilder(deviceId = payloadBuilder.uid).build

    when(service.ingest(any[String])).thenReturn(Future {deviceLogRecord})

    val request = FakeRequest(POST, "/log").withJsonBody(Json.parse(payload))

    val result: Future[Result] = controller.add().apply(request)

    val bodyText: String = contentAsString(result)

    status(result) shouldEqual CREATED
    bodyText should include(payloadBuilder.uid)
    verify(service, times(1)).ingest(any[String])
  }

  behavior of "receiving a invalid payload"
  it should "successfully handle the request" in {
    when(service.ingest(any[String])).thenReturn(Future { mock[DeviceLogRecord] })

    val request = FakeRequest(POST, "/log").withTextBody("any-text")

    val result: Future[Result] = controller.add().apply(request)

    val resultPayload = contentAsJson(result).as[Map[String, String]]

    status(result) shouldEqual BAD_REQUEST
    resultPayload("message") should include("Invalid request payload!")
    verifyZeroInteractions(service)
  }

}
