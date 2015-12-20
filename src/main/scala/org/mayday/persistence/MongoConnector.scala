package org.mayday.persistence

import java.util.UUID

import org.mayday.api.UserResponse
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONArray, BSONDocument}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by rayanral on 12/6/15.
  */
object MongoConnector {

  private val driver = new MongoDriver
  private val connection = driver.connection(List("localhost"))
  private val db = connection.db("mayday")
  val eventsTable = db.collection[BSONCollection]("events")
  val usersTable = db.collection[BSONCollection]("users")

}


trait UserOperations {

  def createUser(user: User) = {
    MongoConnector.usersTable.insert(user)
  }

  def rateUser(userId: UUID, rate: Rate) = {
    val selector = BSONDocument("userId" -> userId.toString)
    val update = BSONDocument("$push" -> BSONDocument("rates" -> rate))
    MongoConnector.usersTable.update(selector, update, upsert = true)
  }

  def getUser(userId: UUID) = {
    val selector = BSONDocument("userId" -> userId.toString)
    val userOption = MongoConnector.usersTable.find(selector).cursor[User](ReadPreference.primary).headOption
    userOption map {
      case None =>
        ErrorMessage("User not found")
      case Some(User(_, name, phone, email, fbLink, vkLink, twitterLink, _, _, rates, comments, lastCoords)) =>
        val rating = rates.map(_.mark).sum / rates.length
        UserResponse(name, phone, email, fbLink, vkLink, twitterLink, rating, comments, lastCoords)
    }
  }

}

trait EventOperations {


  def createEvent(event: Event) = {
    MongoConnector.eventsTable.insert(event)
  }

  def getEvents(coords: Coordinate, radius: Int) = {
    val selector =
      BSONDocument("coords" ->
        BSONDocument("$near" ->
          BSONDocument(
            "$geometry" -> BSONDocument("type" -> "Point", "coordinates" -> BSONArray(coords.x, coords.y)),
            "$maxDistance" -> radius
          )
        )
      )
    MongoConnector.eventsTable.find(selector).cursor[Event].collect[List]()
  }

  def addComment(eventId: UUID, comment: EventComment) = {
    val selector = BSONDocument("eventId" -> eventId.toString)
    val update = BSONDocument("$push" -> BSONDocument("comments" -> comment))
    MongoConnector.eventsTable.update(selector, update, upsert = true)
  }
}