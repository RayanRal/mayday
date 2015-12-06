package org.mayday.persistence

import java.util.UUID

/**
  * Created by rayanral on 12/2/15.
  */
case class Coordinate(x: Double, y: Double)


case class User(userId: UUID, name: String, phone: String, email: String,
                fbLink: String, vkLink: String, twitterLink: String,
                alertRadius: Double, alertOn: Boolean, rating: Int,
                comments: List[String], lastCoords: Coordinate)

case class Event(eventId: UUID, createdBy: String, coords: Coordinate, description: String, comments: List[String])
