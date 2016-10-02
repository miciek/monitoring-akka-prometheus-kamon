package com.michalplachta.shoesorter

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import com.michalplachta.shoesorter.Domain.Junction
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}
import kamon.Kamon

object SortingDecider {
  def props = Props[SortingDecider]

  def shardName = "sortingDecider"

  val extractShardId: ShardRegion.ExtractShardId = {
    case WhereShouldIGo(junction, _) =>
      (junction.id % 2).toString
  }

  val extractEntityId: ShardRegion.ExtractEntityId = {
    case m: WhereShouldIGo =>
      (m.junction.id.toString, m)
  }
}

class SortingDecider extends Actor with ActorLogging {
  var totalContainers = 0

  def receive = {
    case WhereShouldIGo(junction, container) =>
      val decision = Decisions.whereShouldContainerGo(junction, container)
      log.debug(s"Decision: $decision")
      recordJunctionMetrics(junction)
      sender ! Go(decision)
  }

  def recordJunctionMetrics(junction: Junction): Unit = {
    if(junction.id == 1) totalContainers = totalContainers + 1
    if(junction.id == 5) totalContainers = totalContainers - 1
    Kamon.metrics.gauge("containers_total")(0L).record(totalContainers)
  }
}


