package infrastructure.parsing

import org.joda.time.DateTime
import play.api.libs.json.{Format, JodaReads, JodaWrites}

trait JodaDateTimeReadersAndWriters {
  val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
  implicit val dateFormat: Format[DateTime] = Format[DateTime](
    JodaReads.jodaDateReads(pattern),
    JodaWrites.jodaDateWrites(pattern)
  )
}
