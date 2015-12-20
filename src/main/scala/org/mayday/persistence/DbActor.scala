package org.mayday.persistence

import akka.actor.{Actor, ActorLogging}
import org.mayday.api.{EventCommentRequest, CreateEventRequest}

/**
  * Created by rayanral on 12/13/15.
  */
class DbActor
  extends Actor with ActorLogging
  with EventOperations
  with UserOperations {

  override def receive: Receive = {
    case CreateEventRequest(eventId, userId, xCoord, yCoord, description) =>
      val event = Event(eventId, userId, Coordinate(xCoord, yCoord), description, List.empty)
      createEvent(event)
    case EventCommentRequest(eventId, commenterId, commenterName, text) =>
      val comment = Comment(commenterId, "", text)
      addComment(eventId, comment)
    case GetUser(userId) =>
      sender ! getUser(userId)
    case GetEvents(x: Double, y: Double, radius: Int) =>
      sender ! getEvents(Coordinate(x, y), radius)
  }

}
