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
package de.dennishoersch.util.jersey.inject

import java.lang.annotation.Annotation
import java.lang.reflect.ParameterizedType
import javax.ws.rs.core.MultivaluedMap

import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.ProviderServices
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor

class OptionExtractor(extractor: MultivaluedParameterExtractor) extends MultivaluedParameterExtractor {

  def getName: String = extractor.getName

  def getDefaultStringValue: String = extractor.getDefaultStringValue

  def extract(parameters: MultivaluedMap[String, String]): Object = Option(extractor.extract(parameters))
}

abstract class AbstractOptionParamInjectableProvider[A <: Annotation](
  services: ProviderServices)
  extends AbstractParamInjectableProvider[A](services) {

  override final def isExtractable(parameter: Parameter): Boolean =
    parameter.getSourceName != null && !parameter.getSourceName.isEmpty &&
    parameter.getParameterClass.isAssignableFrom(classOf[Option[_]]) &&
    parameter.getParameterType.isInstanceOf[ParameterizedType]

  override final def createExtractor(parameter: Parameter) = new OptionExtractor(unpackedExtractorOf(parameter))


}


