package io_log_ingestion

import com.google.inject.{Inject, Singleton}
import infrastructure.parsing.DeviceLogRecordReadersAndWriters
import play.api.Logger
import play.api.http.HttpVerbs
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source
import scala.util.Try

@Singleton
class LogController @Inject()(service: LogIngestionService) extends Controller with HttpVerbs
  with DeviceLogRecordReadersAndWriters {

  val logger = Logger(getClass)

  def add: Action[AnyContent] = Action.async { implicit request =>
    val requestType = request.contentType
    val charset = request.charset
    val acceptedTypes = request.acceptedTypes
    val mediaType = request.mediaType
    val bodyAsText = Try(request.body.asText).toOption
    val bodyAsForm = Try(request.body.asFormUrlEncoded).toOption
    val bodyAsJson = Try(request.body.asJson).toOption
    val bodyAsMultipart = Try(request.body.asMultipartFormData).toOption
    val bodyAsXml = Try(request.body.asXml).toOption
    val bodyAsRaw = Try(request.body.asRaw.map(_.toString)).toOption
    val bodyAsRawFile = Try(request.body.asRaw.map(_.asFile.toString)).toOption
    val fileContent = Try(request.body.asRaw.map(f => Source.fromFile(f.asFile.getAbsolutePath).getLines.mkString)).toOption
    val log =
      s"""
         |{
         |  "requestType": "$requestType"
         |  "charset": "$charset"
         |  "acceptedTypes": "$acceptedTypes"
         |  "mediaType": "$mediaType"
         |  "bodyAsText": "$bodyAsText"
         |  "bodyAsForm": "$bodyAsForm"
         |  "bodyAsJson": "$bodyAsJson"
         |  "bodyAsMultipart": "$bodyAsMultipart"
         |  "bodyAsXml": "$bodyAsXml"
         |  "bodyAsRaw": "$bodyAsRaw"
         |  "bodyAsRawFile": "$bodyAsRawFile"
         |  "fileContent": "$fileContent"
         |}
       """.stripMargin
    logger.info(s"Any log received! $request | ${request.body} | $log")
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
