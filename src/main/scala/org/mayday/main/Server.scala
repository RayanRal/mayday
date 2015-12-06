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
    val configResource = System.getProperty("config.resource") match {
      case x: String => x
      case _  =>        "server.conf"
    }

    val serverSettings: Config = ConfigFactory.load(configResource,
      ConfigParseOptions.defaults().setAllowMissing(false),
      ConfigResolveOptions.defaults())

    ActorSystem("Mayday", serverSettings)
  }

  class Application(val actorSystem: ActorSystem) {
    val rootService = actorSystem.actorOf(Props(new RoutedHttpService()))
    def config = actorSystem.settings.config

    // Create and start the spray-can HttpServer
    val httpServer = IO(Http)(system)

    val address = config.getString("server.address")
    val port    = config.getInt("server.port")
    httpServer.tell(Http.Bind(rootService, address, port), rootService)
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
