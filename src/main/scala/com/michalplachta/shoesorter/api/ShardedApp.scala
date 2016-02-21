package com.michalplachta.shoesorter.api

import akka.actor.{ActorSystem, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import com.michalplachta.shoesorter.SortingDecider
import com.typesafe.config.ConfigFactory

object ShardedApp extends App {
  val config = ConfigFactory.load("sharded")
  implicit val system = ActorSystem(config getString "application.name", config)

  ClusterSharding(system).start(
    typeName = SortingDecider.shardName,
    entityProps = SortingDecider.props,
    settings = ClusterShardingSettings(system),
    extractEntityId = SortingDecider.extractEntityId,
    extractShardId = SortingDecider.extractShardId
  )

  val guardian = ClusterSharding(system).shardRegion(SortingDecider.shardName)
  new RestInterface(guardian, config getInt "application.exposed-port")
}
