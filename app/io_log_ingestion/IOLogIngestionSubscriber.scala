package io_log_ingestion

import config.RabbitMQConfig
import infrastructure.QueueSubscriber
import infrastructure.parsing.ParseIngestionEvent
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object IOLogIngestionSubscriber {
  implicit private val config: RabbitMQConfig = RabbitMQConfig()
  implicit private val repository: IOLogRepository = IOLogRepository
  private val logger: Logger = Logger(getClass)

  def apply(): Unit = {
    QueueSubscriber()
      .subscribe(parseAndIngest)
  }

  def parseAndIngest(message: String): Unit = {
    logger.info(s"Received ingestion event '$message'")

    Future.fromTry(ParseIngestionEvent(message))
      .flatMap(IngestIOLog(_))
      .onComplete {
        case Failure(exception) =>
          logger.error(s"There was an error while processing the message. " +
            s"Log was not save in database due to ${exception.getMessage}.")
        case Success(result) =>
          logger.info(s"IO Log was ingested and saved in our database $result")
      }
  }
}
