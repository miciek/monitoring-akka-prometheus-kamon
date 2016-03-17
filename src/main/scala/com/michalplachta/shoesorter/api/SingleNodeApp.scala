package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import com.michalplachta.shoesorter.DecidersGuardian
import com.typesafe.config.ConfigFactory
import kamon.Kamon

object SingleNodeApp extends App {
  Kamon.start()

  val config = ConfigFactory.load()
  implicit val system = ActorSystem(config getString "application.name")

  val guardian = system.actorOf(Props[DecidersGuardian], "guardian")
  new RestInterface(guardian, config getInt "application.exposed-port")
  PrometheusService.start()

  system.registerOnTermination {
    Kamon.shutdown()
  }
}
