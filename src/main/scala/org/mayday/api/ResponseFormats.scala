package org.mayday.api

import java.util.UUID

import org.mayday.persistence.{UserComment, EventComment, Event, Coordinate}
import spray.httpx.SprayJsonSupport
import spray.json._

/**
  * Created by rayanral on 12/20/15.
  */
trait ResponseFormats extends DefaultJsonProtocol with SprayJsonSupport with CommonFormats {

  implicit val UserCommentFormat = jsonFormat3((uuid: UUID, name: String, com: String) => UserComment(uuid, name, com))
  implicit val UserResponseFormat = jsonFormat9(UserResponse)

  implicit val EventCommentFormat = jsonFormat3((uuid: UUID, name: String, com: String) => EventComment(uuid, name, com))
  implicit val EventFormat = jsonFormat5((eventId: UUID, createdUserId: UUID, coords: Coordinate, description: String, comments: List[EventComment]) => Event(eventId, createdUserId, coords, description, comments))

}

object ResponseFormats extends ResponseFormats

case class UserResponse(name: String, phone: String, email: String,
                        fbLink: String, vkLink: String, twitterLink: String,
                        rate: Double, comments: List[UserComment], lastCoords: Coordinate)