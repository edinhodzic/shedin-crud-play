package io.shedin.crud.play

import io.shedin.crud.lib.CrudService
import play.api.libs.json.JsError.toJson
import play.api.libs.json.{Format, JsValue, Reads}
import play.api.mvc.{BodyParser, _}

import scala.concurrent.Future.successful
import scala.concurrent.{ExecutionContext, Future}

abstract class CrudController[T]
(crudService: CrudService[T])
(implicit ec: ExecutionContext, format: Format[T], manifest: Manifest[T]) extends InjectedController {

  def post = Action.async(parseJson[T]) { request =>
    crudService.create(request.body) map format.writes map { jsValue =>
      Created(jsValue).withHeaders(deriveLocationHeader(request, jsValue))
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
      case Some(_) => NoContent
      case _ => NotFound
    }
  }

  def patch(id: String) = Action.async(parseJson[T]) { request =>
    // TODO enforce id on T
    // TODO do partial update here
    crudService.update(id, request.body) map {
      case Some(_) => NoContent
      case _ => NotFound
    }
  }

  def delete(id: String) = Action.async {
    crudService.delete(id) map (deleted => if (deleted) NoContent else NotFound)
  }

  private def parseJson[A](implicit reader: Reads[A], ec: ExecutionContext): BodyParser[A] = BodyParser("json reader") { request =>
    parse.json(request) mapFuture {
      case Left(simpleResult) => successful(Left(simpleResult))
      case Right(jsValue) =>
        jsValue.validate(reader) map { t =>
          successful(Right(t))
        } recoverTotal { jsError =>
          Future(play.api.mvc.Results.BadRequest(toJson(jsError))) map Left.apply
        }
    }
  }

  private def deriveLocationHeader(request: Request[T], jsValue: JsValue): (String, String) = {
    ("Location", s"/${simpleName(manifest)}/${(jsValue \ "id").as[String]}")
  }

  // TODO DRY
  private def simpleName[A](manifest: Manifest[A]): String =
    manifest.runtimeClass.getSimpleName.toLowerCase

}
