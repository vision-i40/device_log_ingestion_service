package config

import com.typesafe.config.{Config, ConfigFactory}

sealed case class Queue(name: String, routingKey: String)

sealed case class RabbitMQConfig(
  host: String,
  port: Int,
  virtualHost: String,
  queue: Queue,
  user: String,
  password: String
)

object RabbitMQConfig {
  private val RABBIT_MQ_CONFIG_PREFIX = "rabbitmq"
  private val HOST = s"$RABBIT_MQ_CONFIG_PREFIX.host"
  private val PORT = s"$RABBIT_MQ_CONFIG_PREFIX.port"
  private val VIRTUAL_HOST = s"$RABBIT_MQ_CONFIG_PREFIX.virtualHost"
  private val QUEUE_NAME = s"$RABBIT_MQ_CONFIG_PREFIX.queue.name"
  private val QUEUE_ROUTING_KEY = s"$RABBIT_MQ_CONFIG_PREFIX.queue.routingKey"
  private val USER = s"$RABBIT_MQ_CONFIG_PREFIX.user"
  private val PASSWORD = s"$RABBIT_MQ_CONFIG_PREFIX.password"

  def apply(config: Config = ConfigFactory.load()): RabbitMQConfig = {
    RabbitMQConfig(
      host = config.getString(HOST),
      port = config.getInt(PORT),
      virtualHost = config.getString(VIRTUAL_HOST),
      queue = Queue(config.getString(QUEUE_NAME), config.getString(QUEUE_ROUTING_KEY)),
      user = config.getString(USER),
      password = config.getString(PASSWORD)
    )
  }

}
