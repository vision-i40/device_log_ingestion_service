package io_log_ingestion.devices.wise

import infrastructure.parsing.JodaDateTimeReadersAndWriters
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.libs.functional.syntax._
import scala.util.Try

object ParseWiseLog extends JodaDateTimeReadersAndWriters {
  implicit val wiseLogReaders: Reads[WiseLog] = (
    (JsPath \ "PE").read[String] and
    (JsPath \ "UID").read[String] and
    (JsPath \ "MAC").read[String] and
    (JsPath \ "TIM").read[DateTime] and
    (JsPath \ "Record").read[List[Int]]
  )(WiseLog.apply _)

  def apply(rawLog: String): Try[WiseLog] = {
    Try(Json.parse(rawLog).as[WiseLog])
  }
}
