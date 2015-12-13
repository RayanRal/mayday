package org.mayday.api

import java.util.UUID

import spray.httpx.SprayJsonSupport
import spray.json._

/**
  * Created by rayanral on 12/13/15.
  */
trait RequestFormats extends DefaultJsonProtocol with SprayJsonSupport {

  implicit object UuidJsonFormat extends RootJsonFormat[UUID] {
    def write(x: UUID) = JsString(x.toString)

    //Never execute this line
    def read(value: JsValue) = value match {
      case JsString(x) => UUID.fromString(x)
      case x => deserializationError("Expected UUID as JsString, but got " + x)
    }
  }

  implicit val CreateEventRequestFormat = jsonFormat5(CreateEventRequest)
  implicit val EventCommentRequestFormat = jsonFormat3(EventCommentRequest)

}

object RequestFormats extends RequestFormats


case class CreateEventRequest(eventId: UUID, createdUserId: UUID, xCoord: Double, yCoord: Double, description: String)

case class EventCommentRequest(eventId: UUID, commenterId: UUID, text: String)