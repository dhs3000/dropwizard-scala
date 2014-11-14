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
import com.sun.jersey.core.util.MultivaluedMapImpl
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import scala.collection.JavaConverters._
import scala.collection.mutable

class GenericExtractor[A <: Traversable[_]](extractor: MultivaluedParameterExtractor, converter: Seq[_] => A) extends MultivaluedParameterExtractor {

  def extract(parameters: MultivaluedMap[String, String]): Object = {
    val buffer = mutable.Buffer[Object]()

    val params = parameters.get(getName)
    if (params != null) {
      for (param <- params.asScala if !param.isEmpty) {
        buffer += extractor.extract(map(getName, param))
      }
    } else if (getDefaultStringValue != null) {
      buffer += getDefaultStringValue
    }
    buffer.toSeq
  }

  private def map(name: String, param: String):MultivaluedMap[String, String] = {
    val result = new MultivaluedMapImpl()
    result.putSingle(name, param)
    result
  }

  def getName: String = extractor.getName

  def getDefaultStringValue: String = extractor.getDefaultStringValue
}

abstract class AbstractCollectionParamInjectableProvider[A <: Annotation](services: ProviderServices)
  extends AbstractParamInjectableProvider[A](services) {

  override final def isExtractable(parameter: Parameter): Boolean =
    parameter.getSourceName != null && !parameter.getSourceName.isEmpty &&
      parameter.getParameterClass.isAssignableFrom(classOf[Seq[_]]) &&
      parameter.getParameterType.isInstanceOf[ParameterizedType]

  def toConcreteSequence(s: Seq[_]): Traversable[_]

  override final def createExtractor(parameter: Parameter): MultivaluedParameterExtractor = new GenericExtractor(unpackedExtractorOf(parameter), toConcreteSequence)
}

abstract class AbstractSeqParamInjectableProvider[A <: Annotation](services: ProviderServices)
  extends AbstractCollectionParamInjectableProvider[A](services) {

  def toConcreteSequence(s: Seq[_]): Traversable[_] = s.toSeq
}