package io_log_ingestion

import com.google.inject.{Inject, Singleton}
import play.api.http.HttpVerbs
import play.api.mvc.AnyContent
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class LogController @Inject() extends Controller with HttpVerbs  {
  def add: Action[AnyContent] = Action.async { implicit request =>
    request.body match {
      case _ => Future {Ok("asd")}
    }
  }
}
