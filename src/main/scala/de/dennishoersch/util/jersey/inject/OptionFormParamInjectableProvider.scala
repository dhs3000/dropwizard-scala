package de.dennishoersch.util.jersey.inject

import javax.ws.rs.FormParam
import javax.ws.rs.core.Context
import javax.ws.rs.ext.Provider
import com.sun.jersey.core.spi.component.ProviderServices
import com.sun.jersey.server.impl.model.parameter.multivalued.MultivaluedParameterExtractor
import com.sun.jersey.spi.inject.Injectable
import com.sun.jersey.api.model.Parameter

@Provider
class OptionFormParamInjectableProvider(
  @Context services: ProviderServices)
  extends AbstractOptionParamInjectableProvider[FormParam](services) {

  def createInjectable(extractor: MultivaluedParameterExtractor, parameter: Parameter) = new FormParamInjectable(extractor)
}

