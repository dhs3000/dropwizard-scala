package de.dennishoersch.util.logging

import org.slf4j.LoggerFactory

trait Logging {
  protected final val logger = LoggerFactory.getLogger(getClass)

  protected final def error(msg: => String, e: => Throwable) =
    if (logger.isErrorEnabled)logger.error(msg, e)

}
