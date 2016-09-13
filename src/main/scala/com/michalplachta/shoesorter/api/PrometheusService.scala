package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import com.monsanto.arch.kamon.prometheus.spray.SprayEndpoint
import spray.routing.SimpleRoutingApp

object PrometheusService extends SimpleRoutingApp {
  def start(exposedPort: Int)(implicit system: ActorSystem): Unit = {
        startServer("0.0.0.0", exposedPort) {
          path("metrics") {
            SprayEndpoint(system).route
          }
        }
  }
}