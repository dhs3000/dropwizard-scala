package de.dennishoersch.util

import javax.servlet.http.HttpServletRequest

import scala.collection.mutable.StringBuilder

package object servlet {

  implicit class RichHttpServletRequest(val request: HttpServletRequest) extends AnyVal {
    def fullPath: String = {
      val builder = new StringBuilder(request.getServletPath)
      if (request.getPathInfo != null) {
        builder.append(request.getPathInfo)
      }
      builder.toString()
    }
  }
}
