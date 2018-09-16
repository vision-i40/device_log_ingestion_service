package io_log_ingestion

import config.RabbitMQConfig
import infrastructure.QueueSubscriber
import infrastructure.parsing.ParseIngestionEvent
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object IOLogIngestionSubscriber {
  private val logger: Logger = Logger(getClass)
  
  def apply(): Unit = {
    implicit val config: RabbitMQConfig = RabbitMQConfig()
    QueueSubscriber()
      .subscribe(parseAndIngest)
  }

  def parseAndIngest(message: String): Unit = {
    implicit val repository: IOLogRepository = IOLogRepository

    logger.info(s"Received ingestion event '$message'")

    Future.fromTry(ParseIngestionEvent(message))
      .flatMap(IngestIOLog(_))
      .onComplete {
        case Failure(exception) =>
          logger.error("There was an error while processing the message. Log was not save in database.", exception)
        case Success(result) =>
          logger.info(s"IO Log was ingested and saved in our database $result")
      }
  }
}
