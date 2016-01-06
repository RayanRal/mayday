package org.mayday.persistence

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import org.specs2.execute.Result
import org.specs2.matcher.ShouldMatchers
import org.specs2.mutable.SpecificationLike
import org.specs2._

/**
  * Created by rayanral on 1/4/16.
  */
class UserOperationsTest extends TestKit(ActorSystem("MaydayTest")) with SpecificationLike with ShouldMatchers {

  val dbActor = TestActorRef[DbActor]

  "Db Actor" should {
    "create a user " in {
      Result.unit {
        dbActor ! CreateUser(UUID.randomUUID(), "name", "8063 400 00 77", "some@email.com")
      }
    }
  }

}
