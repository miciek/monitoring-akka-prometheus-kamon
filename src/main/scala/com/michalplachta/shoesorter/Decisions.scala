package com.michalplachta.shoesorter
import com.michalplachta.shoesorter.Domain.{Container, Junction}
import kamon.Kamon

object Decisions {
  val firstJunctionId = 1
  val lastJunctionId = 5

  def whereShouldContainerGo(junction: Junction, container: Container): String = {
    Thread.sleep(5) // just to simulate resource hunger

    if(junction.id == firstJunctionId) {
      Kamon.metrics.counter(
        "containers_in_total",
        Map("junctionId" -> junction.id.toString)
      ).increment()
    }

    if(junction.id == lastJunctionId) {
      Kamon.metrics.counter(
        "containers_out_total",
        Map("junctionId" -> junction.id.toString)
      ).increment()
      "OUT"
    } else {
      val seed = util.Random.nextInt(10000)
      s"CVR_${junction.id}_$seed"
    }
  }
}
