package org.mayday.persistence

import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
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



}

trait EventOperations {



}