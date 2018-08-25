package infrastructure

import org.joda.time.DateTime

trait QueueEvent {
  val traceId: String
  val deviceId: String
  val receivedAt: DateTime
}