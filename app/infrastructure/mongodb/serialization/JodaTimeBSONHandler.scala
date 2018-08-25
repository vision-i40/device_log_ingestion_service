package infrastructure.mongodb.serialization

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import reactivemongo.bson.{BSONDateTime, BSONHandler}

trait JodaTimeBSONHandler {
  implicit object BSONDateTimeHandler extends BSONHandler[BSONDateTime, DateTime] {
//    val fmt: DateTimeFormatter = ISODateTimeFormat.dateTime()
    def read(time: BSONDateTime) = new DateTime(time.value)
    def write(jodaTime: DateTime) = BSONDateTime(jodaTime.getMillis)
  }
}
