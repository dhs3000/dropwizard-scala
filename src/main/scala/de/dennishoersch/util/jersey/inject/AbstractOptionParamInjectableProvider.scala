package de.dennishoersch.util.jersey.inject

import java.lang.annotation.Annotation
import java.lang.reflect.ParameterizedType

import javax.ws.rs.core.MultivaluedMap

import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ ComponentContext, ComponentScope, ProviderServices }
import com.sun.jersey.server.impl.model.parameter.multivalued.{ MultivaluedParameterExtractor, MultivaluedParameterExtractorFactory, StringReaderFactory }
import com.sun.jersey.spi.inject.{ Injectable, InjectableProvider }

class OptionExtractor(extractor: MultivaluedParameterExtractor) extends MultivaluedParameterExtractor {

  def getName(): String = extractor.getName

  def getDefaultStringValue(): String = extractor.getDefaultStringValue

  def extract(parameters: MultivaluedMap[String, String]): Object = Option(extractor.extract(parameters))
}

abstract class AbstractOptionParamInjectableProvider[A <: Annotation](
  services: ProviderServices)
  extends AbstractParamInjectableProvider[A] {

  private lazy val factory: MultivaluedParameterExtractorFactory = {
    val stringReaderFactory = new StringReaderFactory()
    stringReaderFactory.init(services)

    new MultivaluedParameterExtractorFactory(stringReaderFactory)
  }

  def isExtractable(parameter: Parameter): Boolean = {
    return (parameter.getSourceName != null) && !parameter.getSourceName.isEmpty &&
      parameter.getParameterClass.isAssignableFrom(classOf[Option[_]]) &&
      parameter.getParameterType.isInstanceOf[ParameterizedType]
  }

  def createExtractor(parameter: Parameter) = new OptionExtractor(factory.get(unpack(parameter)))

  private def unpack(param: Parameter): Parameter = {
    val typeParameter = param.getParameterType.asInstanceOf[ParameterizedType].getActualTypeArguments()(0)
    return new Parameter(param.getAnnotations(),
      param.getAnnotation(),
      param.getSource(),
      param.getSourceName(),
      typeParameter,
      typeParameter.asInstanceOf[Class[_]],
      param.isEncoded(),
      param.getDefaultValue());
  }
}


