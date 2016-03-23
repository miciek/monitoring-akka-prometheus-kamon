package com.michalplachta.shoesorter
import com.michalplachta.shoesorter.Domain.{Container, Junction}

object Decisions {
  def whereShouldContainerGo(junction: Junction, container: Container): String = {
    if(junction.id > 999) {
      Thread.sleep(junction.id)
    } else {
      Thread.sleep(5) // just to simulate resource hunger
    }

    if(junction.id != 666) {
      val seed = util.Random.nextInt(10000)
      s"CVR_${junction.id}_$seed"
    } else {
      throw new NullPointerException("undefined is not a function <3")
    }
  }
}
