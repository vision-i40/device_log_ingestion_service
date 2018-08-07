package io_log_ingestion

import java.time.LocalDateTime

import scala.concurrent.Future

sealed case class IOLog(id: String, traceId: String, log: String, receivedAt: LocalDateTime)

trait IOLogRepository {
  def save(ingestionEvent: IngestionEvent): Future[IOLog] = {
    null
  }
}

object IOLogRepository extends IOLogRepository