package de.dennishoersch.util.dropwizard.views.thymeleaf

import de.dennishoersch.util.reflect._
import ognl.ObjectPropertyAccessor

private [thymeleaf] class ScalaObjectPropertyAccessor extends ObjectPropertyAccessor {
  override def getProperty(context: java.util.Map[_, _], target: Object, oname: Object): Object = {
    propertyValue(target, oname.toString).getOrElse(super.getProperty(context, target, oname)).asInstanceOf[Object]
  }
}