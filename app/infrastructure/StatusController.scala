package infrastructure

import javax.inject._
import play.api.mvc._

@Singleton
class StatusController @Inject() extends Controller {
  def index = Action { implicit request =>
    Ok("Server is up and running!")
  }
}
