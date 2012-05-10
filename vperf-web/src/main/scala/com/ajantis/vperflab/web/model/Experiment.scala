package com.ajantis.vperflab.web.model

import net.liftweb.mapper._


/**
 * @author Dmitry Ivanov (divanov@ambiqtech.ru)
 *         Ambiq Technology Ltd
 */

class Experiment extends LongKeyedMapper[Experiment] with IdPK {

  def getSingleton = Experiment

  object name extends MappedString(this, 128)

  object iterations extends MappedLong(this)

  object clients extends MappedLong(this)

}

object Experiment extends Experiment with LongKeyedMetaMapper[Experiment] {
  override def dbTableName = "experiments"
}


