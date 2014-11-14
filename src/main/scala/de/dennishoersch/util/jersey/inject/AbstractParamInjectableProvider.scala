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

import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ComponentContext, ComponentScope, ProviderServices}
import com.sun.jersey.server.impl.model.parameter.multivalued.{MultivaluedParameterExtractor, MultivaluedParameterExtractorFactory, StringReaderFactory}
import com.sun.jersey.spi.inject.{Injectable, InjectableProvider}

abstract class AbstractParamInjectableProvider[A <: Annotation](services: ProviderServices)
  extends InjectableProvider[A, Parameter] {

  private lazy val factory: MultivaluedParameterExtractorFactory = {
    val stringReaderFactory = new StringReaderFactory()
    stringReaderFactory.init(services)

    new MultivaluedParameterExtractorFactory(stringReaderFactory)
  }

  private def unpack(param: Parameter): Parameter = {
    val typeParameter = param.getParameterType.asInstanceOf[ParameterizedType].getActualTypeArguments.head
    new Parameter(param.getAnnotations,
      param.getAnnotation,
      param.getSource,
      param.getSourceName,
      typeParameter,
      typeParameter.asInstanceOf[Class[_]],
      param.isEncoded,
      param.getDefaultValue)
  }

  protected final def unpackedExtractorOf(parameter: Parameter) = factory.get(unpack(parameter))

  final def getScope = ComponentScope.PerRequest

  final def getInjectable(ic: ComponentContext, a: A, c: Parameter): Injectable[_] = {
    if (isExtractable(c)) {
      createInjectable(createExtractor(c), c)
    }
    else null
  }
  
  def isExtractable(parameter: Parameter): Boolean
  def createExtractor(parameter: Parameter): MultivaluedParameterExtractor
  def createInjectable(extractor: MultivaluedParameterExtractor, parameter: Parameter): Injectable[_]
}


