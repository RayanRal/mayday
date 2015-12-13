package org.mayday.persistence

import java.util.UUID

/**
  * Created by rayanral on 12/6/15.
  */
object EventOperationsTest extends App with EventOperations {

  createEvent(Event(UUID.randomUUID(), UUID.randomUUID(), Coordinate(5, 2), "description", List.empty))

}
