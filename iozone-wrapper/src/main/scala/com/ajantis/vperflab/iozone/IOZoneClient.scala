package com.ajantis.vperflab.iozone

import java.io._
import scala.actors._
import scala.actors.Actor._
import com.ajantis.vperflab.client.vPerfClient

/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */
class IOZoneClient(val iozoneHomePath: String) extends vPerfClient {

  private val caller = self
  private val WAIT_TIME = 60000

  private val sampleFileName = "sample.tmp"
  private val sampleFileSize = 20000 // 50 Mb

  require( { val file = new File(iozoneHomePath); file.exists() && file.isDirectory },
    "IOZone home doesn't exist or not a directory!")

  override def run(): Option[String] =
    run(iozoneHomePath+"/iozone -f "+ sampleFileName + " -s " +sampleFileSize)


  private val reader = actor {

    var continue = true
    loopWhile(continue){
      reactWithin(WAIT_TIME) {
        case TIMEOUT =>
          caller ! "react timeout"
        case proc:Process =>

          val streamReader = new java.io.InputStreamReader(proc.getInputStream)
          val bufferedReader = new java.io.BufferedReader(streamReader)
          val stringBuilder = new java.lang.StringBuilder()
          var line:String = null
          while({line = bufferedReader.readLine; line != null}){
            stringBuilder.append(line)
            stringBuilder.append("\n")
          }
          bufferedReader.close
          continue = false
          caller ! stringBuilder.toString
      }
    }
  }

  private def run(command:String): Option[String] = {

    val args = command.split(" ")
    val processBuilder = new ProcessBuilder(args: _* )
    processBuilder.redirectErrorStream(true)
    val proc = processBuilder.start()

    //Send the proc to the actor, to extract the console output.
    reader ! proc

    var commandResult : Option[String] = None
    receiveWithin(WAIT_TIME) {
      case TIMEOUT => commandResult = None
      case result:String => commandResult = Some(result)
    }

    commandResult
  }
}



