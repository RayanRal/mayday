package org.mayday.api

import java.util.UUID

import spray.httpx.SprayJsonSupport
import spray.json._

/**
  * Created by rayanral on 12/13/15.
  */
trait RequestFormats extends DefaultJsonProtocol with SprayJsonSupport with CommonFormats {

  implicit val CreateEventRequestFormat = jsonFormat5(CreateEventRequest)
  implicit val CreateUserRequestFormat = jsonFormat3(CreateUserRequest)
  implicit val CommentEventRequestFormat = jsonFormat4(CommentEventRequest)

}

object RequestFormats extends RequestFormats


case class CreateEventRequest(eventId: UUID, createdUserId: UUID, xCoord: Double, yCoord: Double, description: String)

case class CreateUserRequest(name: String, phone: String, email: String)

case class CommentEventRequest(eventId: UUID, commenterId: UUID, commenterName: String, text: String)