package de.dennishoersch.util.jersey.inject

import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.ext.Provider

import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.ProviderServices
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor

@Provider
class SeqQueryParamInjectableProvider(
  @Context services: ProviderServices)
  extends AbstractSeqParamInjectableProvider[QueryParam](services) {

  def createInjectable(extractor: MultivaluedParameterExtractor, parameter: Parameter) = new FormParamInjectable(extractor)
}

