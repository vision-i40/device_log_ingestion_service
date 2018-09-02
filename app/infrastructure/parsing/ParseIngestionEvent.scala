package infrastructure.parsing

import infrastructure.IngestionEvent
import play.api.libs.json.{Json, Reads, Writes}
import scala.util.Try

trait ParseIngestionEvent extends JodaDateTimeReadersAndWriters {
  implicit val implicitReaders: Reads[IngestionEvent] = Json.reads[IngestionEvent]
  implicit val implicitWriters: Writes[IngestionEvent] = Json.writes[IngestionEvent]
}

object ParseIngestionEvent extends ParseIngestionEvent {
  def apply(logJsonString: String): Try[IngestionEvent] = {
    Try(Json.parse(logJsonString).as[IngestionEvent])
  }
}