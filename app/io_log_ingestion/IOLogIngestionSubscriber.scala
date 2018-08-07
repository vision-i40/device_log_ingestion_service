package io_log_ingestion

import com.google.inject.Singleton
import config.RabbitMQConfig
import infrastructure.QueueSubscriber

@Singleton
class IOLogIngestionSubscriber extends QueueSubscriber {
  implicit private val config: RabbitMQConfig = RabbitMQConfig()

//  subscribe(IngestIOLog(_))
}
