package infrastructure

import org.joda.time.DateTime

sealed case class IngestionEvent(
  traceId: String,
  deviceId: String,
  rawLog: String,
  receivedAt: DateTime
) extends QueueEvent
