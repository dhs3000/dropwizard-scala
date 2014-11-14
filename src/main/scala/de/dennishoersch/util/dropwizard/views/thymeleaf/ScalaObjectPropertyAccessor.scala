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

import de.dennishoersch.util.reflect._
import ognl.ObjectPropertyAccessor

private [thymeleaf] class ScalaObjectPropertyAccessor extends ObjectPropertyAccessor {
  override def getProperty(context: java.util.Map[_, _], target: Object, oname: Object): Object = {
    propertyValue(target, oname.toString).getOrElse(super.getProperty(context, target, oname)).asInstanceOf[Object]
  }
}