package com.ajantis.plab.vperfmanager.web.lib

import org.springframework.web.context.ContextLoader

object SpringAdapter {

  def getBean[T](clazz:Class[T]) : T = {
    ContextLoader.getCurrentWebApplicationContext.getBean(clazz)
  }
}