package io_log_ingestion

import com.google.inject.{Inject, Singleton}
import play.api.http.HttpVerbs
import play.api.mvc.AnyContent
import play.mvc.{Action, Controller}

@Singleton
class LogController @Inject() extends Controller with HttpVerbs  {
  def add: Action[AnyContent] = Action.async { implicit request =>
    request.body
  }
}
