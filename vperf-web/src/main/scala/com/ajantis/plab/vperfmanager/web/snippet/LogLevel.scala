package com.ajantis.plab.vperfmanager.web.snippet

import com.ajantis.plab.vperfmanager.web.model.User
import net.liftweb.util.Props
import net.liftweb.sitemap.Loc
import net.liftweb.widgets.logchanger._

/**
 * @author Dmitry Ivanov
 * iFunSoftware
 */
object LogLevel extends LogLevelChanger with LogbackLoggingBackend {

  override
  def menuLocParams: List[Loc.AnyLocParam] =
    if (Props.productionMode)
      List(User.testSuperUser)
    else
      Nil
}