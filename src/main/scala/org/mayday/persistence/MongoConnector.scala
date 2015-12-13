package org.mayday.persistence

import java.util.UUID

import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import scala.concurrent.ExecutionContext.Implicits.global

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

  def rateUser(userId: UUID, rating: Int) = ???



}

trait EventOperations {


  def createEvent(event: Event) = {
    MongoConnector.eventsTable.insert(event)
  }

  def getEvents(radius: Int): List[Event] = ???

  def addComment(eventId: UUID, comment: Comment) = {
    val selector = BSONDocument("eventId" -> eventId.toString)
    val update = BSONDocument("$push" -> BSONDocument("comments" -> comment))
    MongoConnector.eventsTable.update(selector, update, upsert = true)
  }
}