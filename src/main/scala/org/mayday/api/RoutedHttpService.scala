package org.mayday.api

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import spray.can.Http
import spray.can.Http.Register
import spray.http.HttpRequest
import spray.routing.{Route, RequestContext, HttpService}

/**
  * Created by rayanral on 11/29/15.
  */
class RoutedHttpService(dbActor: ActorRef) extends Actor with HttpService with RequestFormats {

  import context.dispatcher

  implicit def actorRefFactory = context

  val route: Route = {
    path("test") {
      get {
        complete("OK")
      }
    } ~
    path("user" / JavaUUID / "rate" / IntNumber) { (userId, rating) =>
      post {
        complete(s"User $userId rated with $rating")
      }
    } ~
    path("user" / JavaUUID) { userId =>
      get {
        complete(s"Info about user $userId")
      }
    } ~
    path("events" / JavaUUID) { eventId =>
      post {
        entity(as[CreateEventRequest]) { event =>
          dbActor ! event
          complete(s"Event created $eventId")
        }
      }
    } ~
    path("events" / JavaUUID / "comment") { eventId =>
      post{
        entity(as[EventCommentRequest]) { comment =>
          dbActor ! comment
          complete(s"Commented on event $eventId")
        }
      }
    } ~
    path("events") {
      get {
        parameters('radius.as[Int] ? 300) { radius =>
          complete(s"Getting events of $radius")
        }
      }
    }
  }

  def receive = LoggingReceive {
    case _: Http.Connected =>
      sender ! Register(self)

    case request: HttpRequest =>
      route(RequestContext(request, sender, request.uri.path).withDefaultSender(sender))
  }

}
