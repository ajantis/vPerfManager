package com.ajantis.vperflab.client

import scala.Option

/**
 * @author Dmitry Ivanov
 */

trait vPerfClient {

  /*
   * @return an output as String
   */
  def run(): Option[String]

}
