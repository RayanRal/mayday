package org.mayday.api

import java.util.UUID

import org.mayday.persistence.Coordinate
import spray.httpx.SprayJsonSupport
import spray.json._

/**
  * Created by rayanral on 12/20/15.
  */
trait CommonFormats {
  this: DefaultJsonProtocol with SprayJsonSupport =>

  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(x: UUID) = JsString(x.toString)

    def read(value: JsValue) = value match {
      case JsString(x) => UUID.fromString(x)
      case x => deserializationError("Expected UUID as JsString, but got " + x)
    }
  }

  implicit val CoordinateFormat = jsonFormat2(Coordinate)

}
