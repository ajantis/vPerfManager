package com.ajantis.vperflab.workload

import akka.actor.Actor
import akka.util.Duration._
import java.util.concurrent.TimeUnit
import akka.util.{FiniteDuration, Duration}

/**
 * @author Dmitry Ivanov
 */

class WorkloadWorker extends Actor {

  def receive = {
    case Work(iterations) =>
      sender ! ResultWaitTime(doRequests(iterations)) // perform the work
  }

  protected def doRequests(numberOfIterations: Int): Duration = {
    val waitTimes = 1.to(numberOfIterations).map { i =>
      new Request().process()
    }

    aggregateWaitTimes(waitTimes)
  }

  protected def aggregateWaitTimes(waitTimes: Seq[Duration]): FiniteDuration = {
    // just a (sum) / count
    Duration.create(
      (waitTimes.foldLeft(0L)((s, waitTime) => s + waitTime.toMillis)) / waitTimes.length,
      TimeUnit.MILLISECONDS
    )
  }

}

class Request {

  def process(): Duration = {

    val startTime = System.currentTimeMillis;

    //Thread.sleep(10L)
    //TODO here the work is done

    Duration(System.currentTimeMillis - startTime, TimeUnit.MILLISECONDS)
  }
}