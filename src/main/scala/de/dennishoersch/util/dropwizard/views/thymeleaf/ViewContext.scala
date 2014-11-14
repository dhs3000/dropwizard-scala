/*!
 * Copyright 2013-2014 Dennis Hörsch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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