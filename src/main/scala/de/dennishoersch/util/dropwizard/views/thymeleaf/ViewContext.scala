package de.dennishoersch.util.dropwizard.views.thymeleaf

import java.util.Calendar

import scala.collection.JavaConversions._

import org.thymeleaf.context.AbstractContext
import org.thymeleaf.context.WebContextExecutionInfo

import io.dropwizard.views.View

import de.dennishoersch.util.reflect._

private [thymeleaf] class ViewContext(private val view: View) extends AbstractContext {
  setVariables(propertiesWithValuesOf(view))

  override def buildContextExecutionInfo(templateName: String) = new WebContextExecutionInfo(templateName, Calendar.getInstance)
}