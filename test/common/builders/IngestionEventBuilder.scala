package common.builders

import java.util.UUID.randomUUID

import infrastructure.IngestionEvent
import org.joda.time.DateTime

case class IngestionEventBuilder(
  traceId: String = randomUUID.toString,
  deviceId: String = randomUUID.toString,
  rawLog: String = "a-log=as-text",
  receivedAt: DateTime = DateTime.now
) {
  def build: IngestionEvent = {
    IngestionEvent(
      traceId = traceId,
      deviceId = deviceId,
      rawLog = rawLog,
      receivedAt = receivedAt
    )
  }
}
