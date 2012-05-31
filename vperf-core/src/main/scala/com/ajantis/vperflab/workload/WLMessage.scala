package com.ajantis.vperflab.workload

import akka.util.Duration
import com.ajantis.vperflab.model.Experiment

/**
 * @author Dmitry Ivanov
 */

sealed trait WLMessage

case object RunWorkload extends WLMessage

case class Work(iterations: Int) extends WLMessage
case class ResultWaitTime(waitTime: Duration) extends WLMessage
case class WaitTimeApproximation(waitTime: Duration, wlDuration: Duration)
