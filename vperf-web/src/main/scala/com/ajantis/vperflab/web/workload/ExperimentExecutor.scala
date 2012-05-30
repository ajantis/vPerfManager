package com.ajantis.vperflab.web.workload

import com.ajantis.vperflab.workload.WLApp
import net.liftweb.common.{Logger, Box}
import com.ajantis.vperflab.web.lib.SpringAdapter._
import com.ajantis.vperflab.web.lib.DependencyFactory
import com.ajantis.vperflab.web.model.{Iteration, ExecutionStatistics, Experiment}
import java.util.Date

/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */

object ExperimentExecutor {

  private val logger = Logger(this.getClass)
  private val workloadSystem = getBean(classOf[WLApp])

  lazy val currentTime: Box[Date] = DependencyFactory.inject[Date] // inject the date

  def execute(exp: Experiment) {
    val executionStats = ExecutionStatistics.create.experiment(exp).execStartTime(currentTime.getOrElse(new Date()))
    val iterations = exp.getIterations

    try {
      logger.debug("Found "+ iterations.length+" iterations... processing")

      for (iter <- iterations){
        logger.debug("Iteration start: " + currentTime.getOrElse(new Date()))

        workloadSystem.runWorkload(iter.clients.is.toInt, 1)

        Thread.sleep(iter.duration.is)

        logger.debug("Iteration end: " + currentTime.getOrElse(new Date()))

      }

      // TODO executionStats set end time + result
    } catch {
      case e: Exception => logger.error(e.getMessage, e)
    }
  }

}
