package com.ajantis.vperflab.iozone


/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */

object SampleRun extends App {
  val runner = new IOZoneRunner("C:\\iozone")

  runner.runIO() match {
    case Some(s) => println(s)
    case None => println("No result received!")
  }
}