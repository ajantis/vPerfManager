package com.ajantis.vperflab.web.model

import net.liftweb.mapper._

/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */

class ExecutionStatistics extends LongKeyedMapper[ExecutionStatistics] with IdPK {

  def getSingleton = ExecutionStatistics

  object execStartTime extends MappedDateTime(this)

  object execEndTime extends MappedDateTime(this)

  object result extends MappedLong(this)

  object experiment extends MappedLongForeignKey(this, Experiment)

  def execDuration: Long = execEndTime.is.getTime - execStartTime.is.getTime

}

object ExecutionStatistics extends ExecutionStatistics with LongKeyedMetaMapper[ExecutionStatistics] {
  override def dbTableName = "exec_stats"
}

