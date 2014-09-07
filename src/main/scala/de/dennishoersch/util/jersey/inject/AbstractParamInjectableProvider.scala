package de.dennishoersch.util.jersey.inject

import java.lang.annotation.Annotation

import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.{ ComponentContext, ComponentScope, ProviderServices }
import com.sun.jersey.server.impl.model.parameter.multivalued.{ MultivaluedParameterExtractor, MultivaluedParameterExtractorFactory, StringReaderFactory }
import com.sun.jersey.spi.inject.{ Injectable, InjectableProvider }

abstract class AbstractParamInjectableProvider[A <: Annotation]
  extends InjectableProvider[A, Parameter] {

  final def getScope() = ComponentScope.PerRequest

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


