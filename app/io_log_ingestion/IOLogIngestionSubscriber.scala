package io_log_ingestion

import config.RabbitMQConfig
import infrastructure.QueueSubscriber
import infrastructure.parsing.ParseIngestionEvent

import scala.util.Try

object IOLogIngestionSubscriber {
  implicit private val config: RabbitMQConfig = RabbitMQConfig()
  implicit private val repository: IOLogRepository = IOLogRepository

  def apply(): Try[String] = QueueSubscriber().subscribe { message =>
    ParseIngestionEvent(message)
      .map(IngestIOLog(_))
  }
}
