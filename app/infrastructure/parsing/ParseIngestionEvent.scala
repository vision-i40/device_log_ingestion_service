package infrastructure.parsing

import infrastructure.IngestionEvent
import play.api.libs.json.{Json, Reads}

import scala.util.Try

trait ParseIngestionEvent extends JodaDateTimeReadersAndWriters {
  implicit val implicitReaders: Reads[IngestionEvent] = Json.reads[IngestionEvent]
  def apply(logJsonString: String): Try[IngestionEvent] = {
    Try(Json.parse(logJsonString).as[IngestionEvent])
  }
}

object ParseIngestionEvent extends ParseIngestionEvent