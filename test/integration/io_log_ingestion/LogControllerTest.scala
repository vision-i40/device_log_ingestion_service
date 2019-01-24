package integration.io_log_ingestion

import common.builders.IOLogPayloadBuilder
import io_log_ingestion.LogController
import org.scalatest.FlatSpec
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.{POST, contentAsString}

import scala.concurrent.Future

class LogControllerTest extends FlatSpec {

  val controller = new LogController()

  behavior of "receiving a json payload"

  it should "successfully handle the request" in {
    val payload = IOLogPayloadBuilder().build

    val request = FakeRequest(POST, "/log").withJsonBody(Json.parse(payload))

    val result: Future[Result] = controller.add().apply(request)
    val bodyText: String = contentAsString(result)
    val actualLogEvent = Json.parse(bodyText).as[LogEvent]


  }

}
