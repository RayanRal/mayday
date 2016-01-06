package org.mayday.persistence

import akka.actor.{Actor, ActorLogging}
import org.mayday.api.{DefaultTimeout, CommentEventRequest, CreateEventRequest}
import akka.pattern.pipe

/**
  * Created by rayanral on 12/13/15.
  */
class DbActor extends Actor with ActorLogging
  with EventOperations
  with UserOperations
  with DefaultTimeout {

  implicit val ec = context.dispatcher

  override def receive: Receive = {
    case CreateUser(userId, name, phone, email) =>
      val user = User(userId, name, phone, email, None, None, None, 100, true, List.empty, List.empty, Coordinate(0, 0))
      createUser(user)
    case CreateEventRequest(eventId, userId, xCoord, yCoord, description) =>
      val event = Event(eventId, userId, Coordinate(xCoord, yCoord), description, List.empty)
      createEvent(event)
    case CommentEventRequest(eventId, commenterId, commenterName, text) =>
      val comment = EventComment(commenterId, "", text)
      addComment(eventId, comment)
    case GetUser(userId) =>
      pipe(getUser(userId)) to sender
    case GetEvents(x: Double, y: Double, radius: Int) =>
      pipe(getEvents(Coordinate(x, y), radius)) to sender
  }

}
