package com.ajantis.vperflab.web.workload

import com.ajantis.vperflab.workload.WLApp
import com.ajantis.vperflab.web.model.{ExecutionStatistics, Experiment}
import java.util.Date
import com.ajantis.plab.vperfmanager.web.lib.DependencyFactory
import net.liftweb.common.{Logger, Box}
import com.ajantis.vperflab.web.lib.SpringAdapter._

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

    try {
      workloadSystem.runWorkload(exp.clients.is.toInt, exp.iterations.is.toInt)
      // TODO executionStats set end time + result
    } catch {
      case e: Exception => logger.error(e.getMessage, e)
    }
  }

}
