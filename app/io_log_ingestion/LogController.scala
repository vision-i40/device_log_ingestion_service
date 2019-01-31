package io_log_ingestion

import com.google.inject.{Inject, Singleton}
import infrastructure.parsing.DeviceLogRecordReadersAndWriters
import play.api.Logger
import play.api.http.HttpVerbs
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class LogController @Inject()(service: LogIngestionService) extends Controller with HttpVerbs
  with DeviceLogRecordReadersAndWriters {

  val logger = Logger(getClass)

  def add: Action[AnyContent] = Action.async { implicit request =>
    request.body match {
      case AnyContentAsJson(payload) =>
        logger.info(s"Received json message '$payload'")
        service
          .ingest(payload.toString)
          .map(deviceLogRecord => Created(Json.toJson(deviceLogRecord)))
      case _ => Future { BadRequest(Json.toJson(Map("message" -> "Invalid request payload!"))) }
    }
  }
}
