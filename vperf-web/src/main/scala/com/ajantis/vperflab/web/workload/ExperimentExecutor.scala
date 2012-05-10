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

  def execute(exp: Experiment) = {
    val executionStats = ExecutionStatistics.create.experiment(exp).execStartTime(currentTime)

    try {
      workloadSystem.runWorkload(exp.clients.is, exp.iterations.is)
      // TODO executionStats set end time + result
    } catch {
      case _ => logger.error(_)
    }
  }

}
