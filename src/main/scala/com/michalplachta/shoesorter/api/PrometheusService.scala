package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import com.monsanto.arch.kamon.prometheus.Prometheus
import kamon.Kamon
import spray.routing.SimpleRoutingApp

object PrometheusService extends SimpleRoutingApp {
  def start(exposedPort: Int)(implicit system: ActorSystem): Unit = {
    Kamon.start()

    startServer("0.0.0.0", exposedPort) {
      path("metrics") {
        Kamon(Prometheus).route
      }
    }
  }
}