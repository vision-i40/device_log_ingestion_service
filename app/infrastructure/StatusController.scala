package infrastructure

import infrastructure.mongodb.Connection
import infrastructure.mongodb.serialization.DeviceLogRecordBSONHandler
import javax.inject._
import play.api.libs.json.Json
import play.api.mvc._
import reactivemongo.api.ReadConcern
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext

@Singleton
class StatusController @Inject()(implicit ec: ExecutionContext) extends Controller with DeviceLogRecordBSONHandler {
  def index: Action[AnyContent] = Action.async { implicit request =>
    Connection()
      .collection("device_logs")
      .map(_.count(Some(BSONDocument()), None, 0, None, ReadConcern.Local))
      .map { _ =>
        Ok(Json.obj(
          "status" -> "RUNNING"
        ))
      }
  }
}
