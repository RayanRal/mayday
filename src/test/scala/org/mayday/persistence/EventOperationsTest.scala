package org.mayday.persistence

import java.util.UUID

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by rayanral on 12/6/15.
  */
object EventOperationsTest extends App with EventOperations {

//  createEvent(Event(UUID.randomUUID(), UUID.randomUUID(), Coordinate(5, 2), "description", List.empty))

  val r = getEvents(Coordinate(5, 2), 10000000)
  val duration = Duration(1000L, "s")
  println(Await.result[List[Event]](r, duration))

}


object UserOperationTest extends App with UserOperations {

  createUser(User(UUID.randomUUID(), "name", "8063 400 00 77", "some@email.com", None, None, None, 0, true, List.empty, List.empty, Coordinate(0, 0)))

}