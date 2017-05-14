package com.michalplachta.shoesorter

import akka.actor.{ ActorLogging, Actor, Props }
import com.michalplachta.shoesorter.Messages._

class DecidersGuardian extends Actor with ActorLogging {
  def receive = {
    case m: WhereShouldIGo ⇒
      val name = s"decider-${m.junction.id}"
      val actor = context.child(name) getOrElse context.actorOf(Props[SortingDecider], name)
      log.debug(s"Forwarding $m to actor $actor")
      actor forward m
  }
}
