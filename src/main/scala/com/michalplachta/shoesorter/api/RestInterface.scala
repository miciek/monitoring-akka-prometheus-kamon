package com.michalplachta.shoesorter.api

import akka.actor.{ActorRef, ActorSystem}
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}
import kamon.trace.Tracer

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class RestInterface(decider: ActorRef, exposedPort: Int)(implicit system: ActorSystem) extends SLF4JLogging {
  implicit val timeout = Timeout(5 seconds)
  implicit val materializer = ActorMaterializer()

  val route =
    path("junctions" / IntNumber / "decisionForContainer" / IntNumber) { (junctionId, containerId) =>
      get {
        complete {
          val junction = Junction(junctionId)
          val container = Container(containerId)
          Tracer.withNewContext("DecisionRequest", autoFinish = true) {
            log.info(s"Request for junction $junctionId and container $containerId")
            decider.ask(WhereShouldIGo(junction, container))(5 seconds).mapTo[Go]
          }
        }
      }
    }

  val binding = Http().bindAndHandle(route, "0.0.0.0", exposedPort)
  binding.onSuccess {
    case binding =>
      log.info("Successfully started the HTTP server on port {}", exposedPort)
  }
  binding.onFailure {
    case binding =>
      log.error("Server not started")
  }
}
