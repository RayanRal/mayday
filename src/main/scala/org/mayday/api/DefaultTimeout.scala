package org.mayday.api

import java.util.concurrent.TimeUnit._
import akka.util._

/**
  * Created by rayanral on 12/20/15.
  */
trait DefaultTimeout {

  implicit val timeout = Timeout(10, SECONDS)

}
