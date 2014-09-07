package de.dennishoersch.util.jersey.inject

import javax.ws.rs.QueryParam
import javax.ws.rs.core.Context
import javax.ws.rs.ext.Provider
import com.sun.jersey.core.spi.component.ProviderServices
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import com.sun.jersey.api.model.Parameter

@Provider
class OptionQueryParamInjectableProvider(
  @Context services: ProviderServices)
  extends AbstractOptionParamInjectableProvider[QueryParam](services) {

  def createInjectable(extractor: MultivaluedParameterExtractor, parameter: Parameter) = new QueryParamInjectable(extractor, !parameter.isEncoded)
}
