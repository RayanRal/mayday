package org.mayday.main

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.typesafe.config.{ConfigResolveOptions, ConfigParseOptions, ConfigFactory, Config}
import org.mayday.api.RoutedHttpService
import spray.can.Http

import scala.concurrent.duration._

/**
  * Created by rayanral on 12/1/15.
  */
trait ServerOperations {

  val system = {
//    val configResource = System.getProperty("config.resource") match {
//      case x: String => x
//      case _  =>
//        System.setProperty("config.resource", "server.conf")
//        "server.conf"
//    }
//
//    val serverSettings: Config = ConfigFactory.load(configResource,
//      ConfigParseOptions.defaults().setAllowMissing(false),
//      ConfigResolveOptions.defaults())

//    ActorSystem("Mayday", serverSettings)
    ActorSystem("Mayday")
  }

  class Application(val actorSystem: ActorSystem) {
    val rootService = actorSystem.actorOf(Props(new RoutedHttpService()))

    // Create and start the spray-can HttpServer
    val httpServer = IO(Http)(system)

    val interface = "127.0.0.1"   //config.getString("server.bind")
    val webPort = 8080            //config.getInt("server.port")
    httpServer.tell(Http.Bind(rootService, interface, webPort), rootService)
  }

  def startup(): Unit = {
    new Application(system)
  }

  def shutdown(): Unit = {
    system.shutdown()
  }
}

object Server extends ServerOperations {
  def main(args: Array[String]) {
    startup()

    sys.addShutdownHook {
      shutdown()
    }
  }
}
