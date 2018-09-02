package common

import com.rabbitmq.client.AMQP.{BasicProperties, Exchange, Queue}
import com.rabbitmq.client.{Channel, Connection, ConnectionFactory}
import config.RabbitMQConfig
import play.api.http.MimeTypes
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object QueueHelper {
  private val config: RabbitMQConfig = RabbitMQConfig()
  private val EXCHANGE_NAME: String = "io_log_exchange_test"
  private val EXCHANGE_TYPE: String = "direct"
  private val ROUTING_KEY: String = "io_log.ingestion"
  private val connection: Connection = connect()
  private val channel: Channel = connection.createChannel()

  def publish(message: String): Unit = {
    val messageBodyBytes = message.getBytes

    val amqpProperties = new BasicProperties.Builder()
      .contentType(MimeTypes.JSON)
      .priority(10)
      .build()

    channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, amqpProperties, messageBodyBytes)
  }

  def setupExchange: (Exchange.DeclareOk, Queue.DeclareOk, Queue.BindOk) = {
    (
      channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true),
      channel.queueDeclare(config.queue.name, true, false, false, null),
      channel.queueBind(config.queue.name, EXCHANGE_NAME, config.queue.routingKey)
    )
  }

  def countMessages(queueName: String): Long = channel.messageCount(queueName)

  def reset: Queue.PurgeOk = {
    channel.queuePurge(config.queue.name)
  }

  private def connect(): Connection = {
    val factory = new ConnectionFactory

    factory.setUsername(config.user)
    factory.setPassword(config.password)
    factory.setVirtualHost(config.virtualHost)
    factory.setHost(config.host)
    factory.setPort(config.port)

    factory.newConnection
  }
}
