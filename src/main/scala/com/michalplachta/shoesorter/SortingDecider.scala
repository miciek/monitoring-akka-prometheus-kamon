package com.michalplachta.shoesorter

import akka.actor.{Actor, ActorLogging, Props}
import akka.cluster.sharding.ShardRegion
import com.michalplachta.shoesorter.Messages.{Go, WhereShouldIGo}

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
  def receive = {
    case WhereShouldIGo(junction, container) =>
      val decision = Decisions.whereShouldContainerGo(junction, container)
      log.debug(s"Decision: $decision")
      sender ! Go(decision)
  }
}
