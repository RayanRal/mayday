package org.mayday.main

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import com.typesafe.config.{ConfigResolveOptions, ConfigParseOptions, ConfigFactory, Config}
import org.mayday.api.RoutedHttpService
import org.mayday.persistence.DbActor
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
      ConfigParseOptions.defaults().setAllowMissing(true),
      ConfigResolveOptions.defaults())

    ActorSystem("Mayday", serverSettings)
  }

  class Application(val actorSystem: ActorSystem) {
    val dbActor = actorSystem.actorOf(Props[DbActor])
    val rootService = actorSystem.actorOf(Props(new RoutedHttpService(dbActor)))
    def config = actorSystem.settings.config

    // Create and start the spray-can HttpServer
    val httpServer = IO(Http)(system)

    val address = "localhost"//config.getString("server.address")
    val port    = 8080//config.getInt("server.port")
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
