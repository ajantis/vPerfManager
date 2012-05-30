package com.ajantis.vperflab.web.snippet

import net.liftweb.util._
import Helpers._
import net.liftweb.http.{S, SHtml}
import com.ajantis.vperflab.web.model.Experiment
import com.ajantis.vperflab.web.workload.ExperimentExecutor
import xml.NodeSeq
import net.liftweb.common.Full
import net.liftweb.mapper.By
import net.liftweb.http.js.JsCmds

/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */

class Experiments {

  def list = {
    "li *" #> experiments.map( (e: Experiment) => {
      "a *" #> e.name.is &
      "a [href]" #> ("/experiments/" + e.id.is.toString)
    })
  }

  def view = {

    S.param("expId") match {
      case Full(expId) => {
        Experiment.find(By(Experiment.id, expId.toInt)) match {
          case Full(experiment) => {
            ".experiment_name *" #> experiment.name.is &
            ".clients_count *" #> experiment.clients.is &
            ".iterations_count *" #> experiment.iterations.is &
            ".run_btn [onclick]" #> SHtml.ajaxInvoke( () => {runExperiment(experiment); JsCmds.Alert("Running!")}) &
            ".del_btn [onclick]" #> SHtml.ajaxInvoke( () => {
              deleteExperiment(experiment) match {
                case true => JsCmds.Alert("Experiment is deleted.") && JsCmds.RedirectTo("/experiments/")
                case _ => JsCmds.Alert("Experiment is not deleted!") && JsCmds.RedirectTo("")
              }
            })
          }
          case _ => {
            S.warning("Experiment is not found!")
            "* *" #> NodeSeq.Empty
          }
        }
      }
      case _ => {
        S.error("Experiment id is not defined!")
        "* *" #> NodeSeq.Empty
      }
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

  private def create(experimentName: String, iterCount: Int, clientsCount: Int, immediateRun: Boolean = false) {
    if (experimentName != ""){
      val exp = Experiment.create.name(experimentName).clients(clientsCount).iterations(iterCount)
      exp.save()

      if(immediateRun)
        runExperiment(exp)

      S.notice("Experiment is submitted")
    }
    else {
      S.error("Experiment name cannot be empty!")
    }
  }

  private def experiments: List[Experiment] = Experiment.findAll()

  private def runExperiment(exp: Experiment){
    ExperimentExecutor.execute(exp)
  }

  private def deleteExperiment(exp: Experiment) = {
    Experiment.delete_!(exp)
  }
}
