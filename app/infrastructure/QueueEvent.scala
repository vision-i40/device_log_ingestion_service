package infrastructure

import java.time.LocalDateTime

trait QueueEvent {
  val traceId: String
  val receivedAt: LocalDateTime
}