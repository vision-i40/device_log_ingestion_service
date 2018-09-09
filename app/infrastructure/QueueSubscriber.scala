package infrastructure

import com.rabbitmq.client._
import config.RabbitMQConfig
import play.api.Logger
import scala.util.Try

class QueueSubscriber {
  private val logger: Logger = Logger(getClass)

  def subscribe(callback: String => Unit)(implicit config: RabbitMQConfig): Try[String] = {
    logger.info(s"Subscribing to ${config.queue.name}")

    connect(config)
      .flatMap(createChannel)
      .map { channel =>
        channel.basicConsume(config.queue.name, createConsumer(callback, channel))
      }
  }

  private def createConsumer(callback: String => Any, channel: Channel): DefaultConsumer = {
    new DefaultConsumer(channel) {
      override def handleDelivery(consumerTag: String, envelope: Envelope, properties: AMQP.BasicProperties,
                                  body: Array[Byte]): Unit = {
        callback(body.map(_.toChar).mkString)
        channel.basicAck(envelope.getDeliveryTag, false)
      }
    }
  }

  private def createChannel(connection: Connection) : Try[Channel] = {
    Try(connection.createChannel())
  }

  private def connect(config: RabbitMQConfig): Try[Connection] = {
    val factory = new ConnectionFactory

    factory.setUsername(config.user)
    factory.setPassword(config.password)
    factory.setVirtualHost(config.virtualHost)
    factory.setHost(config.host)
    factory.setPort(config.port)

    Try(factory.newConnection)
  }
}

object QueueSubscriber {
  def apply(): QueueSubscriber = new QueueSubscriber()
}
