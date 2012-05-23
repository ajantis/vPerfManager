package com.ajantis.vperflab.web.snippet

import net.liftweb.util._
import Helpers._
import net.liftweb.http.{S, SHtml}
import com.ajantis.vperflab.web.model.Experiment
import com.ajantis.vperflab.web.workload.ExperimentExecutor

/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */

class NewExperiment {

  private def create(experimentName: String, iterCount: Int, clientsCount: Int, immediateRun: Boolean = false) {
    if (experimentName != ""){
      val exp = Experiment.create.name(experimentName).clients(clientsCount).iterations(iterCount)
      exp.save()

      if(immediateRun)
        ExperimentExecutor.execute(exp)

      S.notice("Experiment is submitted")
    }
    else {
      S.error("Experiment name cannot be empty!")
    }
  }

  def add = {
    var name = ""
    var iterationsCount = 0
    var clientsCount = 0
    var immediateRun = false

    ".name" #> SHtml.onSubmit( name = _ ) &
    ".iterations_count" #> SHtml.onSubmit(s => iterationsCount = s.toInt) &
    ".run_immediately" #> SHtml.checkbox(immediateRun, immediateRun = _ ) &
    ".clients_count" #> SHtml.onSubmit(s => clientsCount = s.toInt ) &
    ":submit" #> SHtml.onSubmitUnit( () => create(name, iterationsCount, clientsCount, immediateRun) )
  }

}
