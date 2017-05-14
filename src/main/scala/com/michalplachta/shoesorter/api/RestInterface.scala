package com.michalplachta.shoesorter.api

import akka.actor.{ ActorRef, ActorSystem }
import akka.event.slf4j.SLF4JLogging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.{ HttpApp, Route }
import akka.pattern.ask
import com.michalplachta.shoesorter.Domain.{ Container, Junction }
import com.michalplachta.shoesorter.Messages.{ Go, WhereShouldIGo }
import com.monsanto.arch.kamon.prometheus.akka_http.AkkaHttpEndpoint
import kamon.Kamon
import kamon.trace.Tracer

import scala.concurrent.duration._

class RestInterface(decider: ActorRef, exposedPort: Int)(implicit system: ActorSystem) extends SLF4JLogging {
  object WebServer extends HttpApp {
    def route: Route =
      path("junctions" / IntNumber / "decisionForContainer" / IntNumber) { (junctionId, containerId) ⇒
        get {
          complete {
            Tracer.withNewContext("DecisionRequest") {
              val junction = Junction(junctionId)
              val container = Container(containerId)
              Kamon.metrics.counter(
                "api_http_requests_total",
                Map("junctionId" → junctionId.toString, "containerId" → containerId.toString)
              ).increment()

              log.info(s"Request for junction $junctionId and container $containerId")
              decider.ask(WhereShouldIGo(junction, container))(5.seconds).mapTo[Go]
            }
          }
        }
      } ~ path("metrics") {
        AkkaHttpEndpoint(system).route
      }
  }

  WebServer.startServer("0.0.0.0", exposedPort)
}
