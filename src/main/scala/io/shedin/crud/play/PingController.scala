package io.shedin.crud.play

import javax.inject._

import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PingController @Inject()(implicit ec: ExecutionContext) extends Controller {

  def ping = Action.async {
    Future {
      Ok("pong")
    }
  }

}
