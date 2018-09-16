package infrastructure

import com.rabbitmq.client._
import config.RabbitMQConfig
import play.api.Logger
import scala.concurrent.Future
import akka.pattern.after
import akka.actor.ActorSystem
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

class QueueSubscriber {
  private val logger: Logger = Logger(getClass)
  private val retryDelay: FiniteDuration = 2 seconds
  private val actorSystem: ActorSystem = ActorSystem()

  def subscribe(callback: String => Unit)(implicit config: RabbitMQConfig): Future[String] = {
    logger.info(s"Subscribing to ${config.queue.name}")

    connect(config)
      .flatMap(createChannel)
      .map { channel =>
        channel.basicConsume(config.queue.name, createConsumer(callback, channel))
      }
      .recoverWith {
        case _ =>
          logger.error("Failed to connect to queue. Trying again.")
          after(retryDelay, actorSystem.scheduler)(subscribe(callback))
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

  private def createChannel(connection: Connection) : Future[Channel] = {
    Future(connection.createChannel())
  }

  private def connect(config: RabbitMQConfig): Future[Connection] = {
    val factory = new ConnectionFactory

    factory.setUsername(config.user)
    factory.setPassword(config.password)
    factory.setVirtualHost(config.virtualHost)
    factory.setHost(config.host)
    factory.setPort(config.port)

    Future(factory.newConnection)
  }
}

object QueueSubscriber {
  def apply(): QueueSubscriber = new QueueSubscriber()
}
