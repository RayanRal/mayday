package org.mayday

import java.util.UUID

/**
  * Created by rayanral on 12/20/15.
  */
package object persistence {

  case class GetUser(userId: UUID)

  case class RateUser(userId: UUID, rating: Int)

  case class GetEvents(x: Double, y: Double, radius: Int)

  case class ErrorMessage(msg: String)

}
