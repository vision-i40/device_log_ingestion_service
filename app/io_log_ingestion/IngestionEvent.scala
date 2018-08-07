package io_log_ingestion

import java.time.LocalDateTime
import infrastructure.QueueEvent
import play.api.libs.json.{Json, Reads}
import scala.util.Try

sealed case class IngestionEvent(traceId: String, log: String, receivedAt: LocalDateTime) extends QueueEvent

trait ParseIngestionEvent {
  def apply(logJsonString: String): Try[IngestionEvent] = {
    implicit val implicitReaders: Reads[IngestionEvent] = Json.reads[IngestionEvent]

    Try(Json.parse(logJsonString).as[IngestionEvent])
  }
}

object ParseIngestionEvent extends ParseIngestionEvent
