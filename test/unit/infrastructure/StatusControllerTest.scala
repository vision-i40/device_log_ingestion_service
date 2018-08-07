package unit.infrastructure

import infrastructure.StatusController
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._

class StatusControllerTest extends PlaySpec with OneAppPerTest {
  "StatusController GET" should {
    "render the status page from a new instance of controller" in {
      val controller = new StatusController
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
    }

    "render the status page from the application" in {
      val controller = app.injector.instanceOf[StatusController]
      val home = controller.index().apply(FakeRequest())

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
    }

    "render the status page from the router" in {
      val request = FakeRequest(GET, "/").withHeaders("Host" -> "localhost")
      val home = route(app, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/plain")
    }
  }
}
