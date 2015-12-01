package org.mayday.api

import akka.actor.{ActorRefFactory, ActorLogging, Actor}
import akka.event.LoggingReceive
import spray.can.Http
import spray.can.Http.Register
import spray.http.{ChunkedRequestStart, HttpRequest}
import spray.routing.{Route, RequestContext, HttpService}

/**
  * Created by rayanral on 11/29/15.
  */
class RoutedHttpService extends Actor with HttpService {

  import context.dispatcher

  implicit def actorRefFactory = context

  val route: Route = {
    path("test") {
      get {
        complete("OK")
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
