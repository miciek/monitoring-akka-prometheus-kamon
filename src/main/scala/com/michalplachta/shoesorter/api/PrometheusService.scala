package com.michalplachta.shoesorter.api

import akka.actor.ActorSystem
import com.monsanto.arch.kamon.prometheus.Prometheus
import kamon.Kamon
import spray.routing.SimpleRoutingApp

object PrometheusService extends SimpleRoutingApp {
  def start(exposedPort: Int)(implicit system: ActorSystem): Unit = {
    // TODO: kamon-prometheus is not compatible with kamon 0.6 yet
    //    startServer("0.0.0.0", exposedPort) {
    //      path("metrics") {
    //        Kamon(Prometheus).route
    //      }
    //    }
  }
}