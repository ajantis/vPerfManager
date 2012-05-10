package com.ajantis.vperflab.workload

import akka.actor.Actor
import akka.util.Duration._
import java.util.concurrent.TimeUnit
import akka.util.{FiniteDuration, Duration}
import org.slf4j.LoggerFactory
import org.apache.commons.httpclient.methods.GetMethod
import org.apache.commons.httpclient.{HttpException, HttpStatus, HttpClient}
import java.io.IOException

/**
 * @author Dmitry Ivanov
 */

class WorkloadWorker extends Actor {

  val logger = LoggerFactory.getLogger(getClass)
  val httpClient = new HttpClient()

  private val url = "http://stg.spb.ats.ambiqtech.ru:8080/ats/healthcheck"

  def receive = {
    case Work(iterations) =>
      sender ! ResultWaitTime(doRequests(iterations)) // perform the work
  }

  protected def doRequests(numberOfIterations: Int): Duration = {
    val waitTimes = 1.to(numberOfIterations).map { i =>
      new Request(url, httpClient).process()
    }

    aggregateWaitTimes(waitTimes)
  }

  protected def aggregateWaitTimes(waitTimes: Seq[Duration]): FiniteDuration = {
    // just a (sum) / count
    Duration.create(
      (waitTimes.foldLeft(0L)((s, waitTime) => s + waitTime.toMillis)) / waitTimes.length,
      TimeUnit.MILLISECONDS
    )
  }

}

class Request(url: String, client: HttpClient) {

  def process(): Duration = {

    val startTime = System.currentTimeMillis;

    //TODO here the work is done
    val method = new GetMethod(url)

    try {
      val statusCode = client.executeMethod(method)

      if (statusCode != HttpStatus.SC_OK) {
        System.err.println("Method failed: " + method.getStatusLine)
      }

      val responseBody = method.getResponseBody;
      //System.out.println(new String(responseBody));

    } catch {
        case e: HttpException => {
          System.err.println("Fatal protocol violation: " + e.getMessage)
          e.printStackTrace()
        }
        case e: IOException => {
          System.err.println("Fatal transport error: " + e.getMessage)
          e.printStackTrace()
        }
    } finally {
      method.releaseConnection();
    }

    Duration(System.currentTimeMillis - startTime, TimeUnit.MILLISECONDS)
  }
}