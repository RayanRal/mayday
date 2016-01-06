package org.mayday.api

import akka.actor.{ActorRef, Actor}
import akka.event.LoggingReceive
import org.mayday.persistence._
import spray.can.Http
import spray.can.Http.Register
import spray.http._
import spray.routing.{Route, RequestContext, HttpService}
import akka.pattern.ask
import spray.json._

/**
  * Created by rayanral on 11/29/15.
  */
class RoutedHttpService(dbActor: ActorRef) extends Actor
  with HttpService
  with RequestFormats
  with ResponseFormats
  with DefaultTimeout {

  import context.dispatcher

  implicit def actorRefFactory = context

  val route: Route = {
    path("test") {
      get {
        complete("OK")
      }
    } ~
    path("user" / JavaUUID) { userId =>
      post {
        entity(as[CreateUserRequest]) { cu =>
          dbActor ! CreateUser(userId, cu.name, cu.phone, cu.email)
          complete(s"User $userId created succesfully")
        }
      }
    } ~
    path("user" / JavaUUID / "rate" / IntNumber) { (userId, rating) =>
      post {
        dbActor ! RateUser(userId, rating)
        complete(s"User $userId rated with $rating")
      }
    } ~
    path("user" / JavaUUID) { userId =>
      get {
        ctx =>
          (dbActor ? GetUser(userId)).mapTo[Either[ErrorMessage, UserResponse]].map {
            case Left(err) =>
              complete(HttpResponse(StatusCodes.InternalServerError, HttpEntity(ContentTypes.`application/json`, err.msg)))
            case Right(ur) =>
              complete(HttpResponse(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, ur.toJson.toString)))
          }
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
        entity(as[CommentEventRequest]) { comment =>
          dbActor ! comment
          complete(s"Commented on event $eventId")
        }
      }
    } ~
      path("events") {
        get {
          parameters('x.as[Double]) { x =>
            parameters('y.as[Double]) { y =>
              parameters('radius.as[Int] ? 300) { radius =>
                ctx =>
                  (dbActor ? GetEvents(x, y, radius)).mapTo[Either[ErrorMessage, List[Event]]].map {
                    case Left(err) =>
                      complete(HttpResponse(StatusCodes.InternalServerError, HttpEntity(ContentTypes.`application/json`, err.msg)))
                    case Right(events) =>
                      complete(HttpResponse(StatusCodes.OK, HttpEntity(ContentTypes.`application/json`, events.toJson.toString)))
                  }

              }
            }
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
