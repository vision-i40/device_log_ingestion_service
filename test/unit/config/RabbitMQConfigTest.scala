package unit.config

import com.typesafe.config.Config
import config.RabbitMQConfig
import org.mockito.Mockito
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class RabbitMQConfigTest extends FlatSpec with Matchers with MockitoSugar with BeforeAndAfterEach {
  val config: Config = mock[Config]

  private val RABBIT_MQ_CONFIG_PREFIX = "rabbitmq"
  private val HOST = s"$RABBIT_MQ_CONFIG_PREFIX.host"
  private val PORT = s"$RABBIT_MQ_CONFIG_PREFIX.port"
  private val VIRTUAL_HOST = s"$RABBIT_MQ_CONFIG_PREFIX.virtualHost"
  private val QUEUE_NAME = s"$RABBIT_MQ_CONFIG_PREFIX.queue.name"
  private val QUEUE_ROUTING_KEY = s"$RABBIT_MQ_CONFIG_PREFIX.queue.routingKey"
  private val USER = s"$RABBIT_MQ_CONFIG_PREFIX.user"
  private val PASSWORD = s"$RABBIT_MQ_CONFIG_PREFIX.password"

  val host = "localhost"
  val port = 5672
  val virtualHost = "/"
  val queueName = "ingestion_queue"
  val routingKey = "io_log.ingestion"
  val user = "rabbitmq"
  val password = "rabbitmq"

  override def beforeEach(): Unit = {
    Mockito.reset(config)

    when(config.getString(HOST)).thenReturn(host)
    when(config.getInt(PORT)).thenReturn(port)
    when(config.getString(VIRTUAL_HOST)).thenReturn(virtualHost)
    when(config.getString(QUEUE_NAME)).thenReturn(queueName)
    when(config.getString(QUEUE_ROUTING_KEY)).thenReturn(routingKey)
    when(config.getString(USER)).thenReturn(user)
    when(config.getString(PASSWORD)).thenReturn(password)
  }

  behavior of "MongoDB Config"
  it should "get host config value" in {
    val rabbitMQConfig: RabbitMQConfig = RabbitMQConfig(config)

    verify(config, times(1)).getString(HOST)
    rabbitMQConfig.host shouldEqual host
  }

  it should "get port config value" in {
    val rabbitMQConfig: RabbitMQConfig = RabbitMQConfig(config)

    verify(config, times(1)).getInt(PORT)
    rabbitMQConfig.port shouldEqual port
  }

  it should "get virtual host config value" in {
    val rabbitMQConfig: RabbitMQConfig = RabbitMQConfig(config)

    verify(config, times(1)).getString(VIRTUAL_HOST)
    rabbitMQConfig.virtualHost shouldEqual virtualHost
  }

  it should "get queue name config value" in {
    val rabbitMQConfig: RabbitMQConfig = RabbitMQConfig(config)

    verify(config, times(1)).getString(QUEUE_NAME)
    rabbitMQConfig.queue.name shouldEqual queueName
  }

  it should "get routing key config value" in {
    val rabbitMQConfig: RabbitMQConfig = RabbitMQConfig(config)

    verify(config, times(1)).getString(QUEUE_ROUTING_KEY)
    rabbitMQConfig.queue.routingKey shouldEqual routingKey
  }

  it should "get username config value" in {
    val rabbitMQConfig: RabbitMQConfig = RabbitMQConfig(config)

    verify(config, times(1)).getString(USER)
    rabbitMQConfig.user shouldEqual user
  }

  it should "get password config value" in {
    val rabbitMQConfig: RabbitMQConfig = RabbitMQConfig(config)

    verify(config, times(1)).getString(PASSWORD)
    rabbitMQConfig.password shouldEqual password
  }
}
