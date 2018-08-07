package io_log_ingestion

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object IngestIOLog {
  def apply(message: String)
           (implicit repository: IOLogRepository, parse: ParseIngestionEvent): Future[IOLog] = {
    Future
      .fromTry(parse(message))
      .flatMap(repository.save)
  }
}
