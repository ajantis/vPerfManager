package com.ajantis.vperflab.iozone


/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */

object SampleRun extends App {
  val runner = new IOZoneClient("C:\\iozone")

  runner.run() match {
    case Some(s) => println(s)
    case None => println("No result received!")
  }
}