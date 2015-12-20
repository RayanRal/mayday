package org.mayday.api

import org.mayday.persistence.{Comment, Event, Coordinate}
import spray.httpx.SprayJsonSupport
import spray.json.DefaultJsonProtocol

/**
  * Created by rayanral on 12/20/15.
  */
trait ResponseFormats extends DefaultJsonProtocol with SprayJsonSupport{

  implicit val CoordinateFormat = jsonFormat2(Coordinate)
  implicit val UserResponseFormat = jsonFormat9(UserResponse)

  implicit val CommentFormat = jsonFormat3(Comment(_, _, _))
  implicit val EventFormat = jsonFormat5(Event(_, _, _, _, _))

}

object ResponseFormats extends ResponseFormats

case class UserResponse(name: String, phone: String, email: String,
                        fbLink: String, vkLink: String, twitterLink: String,
                        rate: Double, comments: List[String], lastCoords: Coordinate)