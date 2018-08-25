package io_log_ingestion

import infrastructure.IngestionEvent
import scala.concurrent.Future

object IngestIOLog {
  def apply(ingestionEvent: IngestionEvent)(implicit repository: IOLogRepository): Future[IOLog] = {
      val ioLog = IOLog(ingestionEvent)

      repository.save(ioLog)
  }
}
