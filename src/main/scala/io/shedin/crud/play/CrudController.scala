package io.shedin.crud.play

import io.shedin.crud.lib.CrudService
import play.api.libs.json.JsError.toJson
import play.api.libs.json.{Format, Reads}
import play.api.mvc.{Action, BodyParser, BodyParsers, Controller, _}

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

abstract class CrudController[T]
(crudService: CrudService[T])
(implicit ec: ExecutionContext, format: Format[T]) extends Controller {

  def post = Action.async(parseJson[T]) { request =>
    crudService.create(request.body) map { t =>
      Created(format.writes(t)) // TODO add location header
    }
  }

  def get(id: String) = Action.async {
    crudService.read(id) map {
      case Some(t) => Ok(format.writes(t))
      case _ => NotFound
    }
  }

  def put(id: String) = Action.async(parseJson[T]) { request =>
    // TODO enforce id on T
    crudService.update(id, request.body) map {
      case Some(t) => NoContent
      case _ => InternalServerError // TODO ???
    }
  }

  def patch(id: String) = Action.async(parseJson[T]) { request =>
    // TODO enforce id on T
    // TODO do partial update here
    crudService.update(id, request.body) map {
      case Some(t) => NoContent
      case _ => InternalServerError // TODO ???
    }
  }

  def delete(id: String) = Action.async {
    crudService.delete(id) map (deleted => if (deleted) NoContent else NotFound)
  }

  private def parseJson[T](implicit reader: Reads[T], ec: ExecutionContext): BodyParser[T] = BodyParser("json reader") { request =>
    BodyParsers.parse.json(request) mapFuture {
      case Left(simpleResult) => successful(Left(simpleResult))
      case Right(jsValue) =>
        jsValue.validate(reader) map { t =>
          successful(Right(t))
        } recoverTotal { jsError =>
          Future(play.api.mvc.Results.BadRequest(toJson(jsError))) map Left.apply
        }
    }
  }

}
