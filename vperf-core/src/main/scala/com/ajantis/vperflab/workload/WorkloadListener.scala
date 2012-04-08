package com.ajantis.vperflab.workload

import akka.actor.Actor

/**
 * @author Dmitry Ivanov
 */

class WorkloadListener extends Actor {

  def receive = {
    case WaitTimeApproximation(waitTime, duration) => {
        println("\n\tWait time approximation: \t\t%s\n\tWorking time: \t%s"
          .format(waitTime, duration))
        context.system.shutdown()
    }
  }

}
