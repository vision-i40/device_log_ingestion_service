package integration.infrastructure

import common.QueueHelper
import config.RabbitMQConfig
import infrastructure.QueueSubscriber
import org.mockito.Mockito.{times, verify}
import org.scalatest.concurrent.Eventually.eventually
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class QueueSubscriberTest extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfterAll {
  implicit private val config = RabbitMQConfig()

  val message = """{"a": "json-message"}"""

  override def beforeAll(): Unit = {
    QueueHelper.reset
    QueueHelper.setupExchange
  }

  behavior of "Receiving a single message on the queue"
  it should "read the message sent to the queue" in {
    QueueHelper.publish(message = message)
    val callbackMock = mock[String => Unit]

    QueueSubscriber.subscribe(callbackMock)

    eventually {
      verify(callbackMock, times(1)).apply(message)
    }
  }
}
