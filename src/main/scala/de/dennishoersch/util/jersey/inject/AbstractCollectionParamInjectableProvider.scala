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