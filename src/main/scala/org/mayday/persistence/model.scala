package org.mayday.persistence

import java.util.UUID

import reactivemongo.bson.{BSONDocument, BSONDocumentWriter, BSONDocumentReader}

/**
  * Created by rayanral on 12/2/15.
  */
case class Coordinate(x: Double, y: Double)


case class User(userId: UUID, name: String, phone: String, email: String,
                fbLink: String, vkLink: String, twitterLink: String,
                alertRadius: Double, alertOn: Boolean, rating: Int,
                comments: List[String], lastCoords: Coordinate)

case class Event(eventId: UUID, createdBy: String, coords: Coordinate, description: String, comments: List[String])


object User {
  implicit object UserReaderWriter extends BSONDocumentReader[User] with BSONDocumentWriter[User] {
    override def read(doc: BSONDocument): User = {
      val userId = UUID.fromString(doc.getAs[String]("userId").get)
      val name = doc.getAs[String]("name").get
      val phone = doc.getAs[String]("phone").get
      val email = doc.getAs[String]("email").get
      val fbLink = doc.getAs[String]("fbLink").get
      val vkLink = doc.getAs[String]("vkLink").get
      val twitterLink = doc.getAs[String]("twitterLink").get
      val alertRadius = doc.getAs[Double]("alertRadius").get
      val alertOn = doc.getAs[Boolean]("alertOn").get
      val rating = doc.getAs[Int]("rating").get
      val comments = doc.getAs[List[String]]("comments").get
      val lastCoordsBson = doc.getAs[BSONDocument]("lastCoords").get
      val xCoord = lastCoordsBson.getAs[Double]("xCoord").get
      val yCoord = lastCoordsBson.getAs[Double]("yCoord").get
      val lastCoords = Coordinate(xCoord, yCoord)

      User(userId, name, phone, email, fbLink, vkLink, twitterLink, alertRadius, alertOn, rating, comments, lastCoords)
    }

    override def write(user: User): BSONDocument = {
      val coords = BSONDocument("xCoord" -> user.lastCoords.x, "yCoord" -> user.lastCoords.y)
      BSONDocument(
        "userId" -> user.userId.toString,
        "name" -> user.name,
        "phone" -> user.phone,
        "email" -> user.email,
        "fbLink" -> user.fbLink,
        "vkLink" -> user.vkLink,
        "twitterLink" -> user.twitterLink,
        "alertRadius" -> user.alertRadius,
        "alertOn" -> user.alertOn,
        "rating" -> user.rating,
        "comments" -> user.comments,
        "lastCoords" -> coords
      )
    }
  }

}

object Event {
  implicit object EventReaderWriter extends BSONDocumentReader[Event] with BSONDocumentWriter[Event] {
    override def read(doc: BSONDocument): Event = {
      val eventId = UUID.fromString(doc.getAs[String]("eventId").get)
      val createdBy = doc.getAs[String]("createdBy").get
      val description = doc.getAs[String]("description").get
      val comments = doc.getAs[List[String]]("comments").get
      val coordsBson = doc.getAs[BSONDocument]("coords").get
      val xCoord = coordsBson.getAs[Double]("xCoord").get
      val yCoord = coordsBson.getAs[Double]("yCoord").get
      val coords = Coordinate(xCoord, yCoord)

      Event(eventId, createdBy, coords, description, comments)
    }

    override def write(event: Event): BSONDocument = {
      val coords = BSONDocument("xCoord" -> event.coords.x, "yCoord" -> event.coords.y)
      BSONDocument(
        "eventId" -> event.eventId.toString,
        "createdBy" -> event.createdBy,
        "coords" -> coords,
        "description" -> event.description,
        "comments" -> event.comments
      )
    }
  }
}
