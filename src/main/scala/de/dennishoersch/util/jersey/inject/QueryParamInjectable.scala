package de.dennishoersch.util.jersey.inject

import com.sun.jersey.api.ParamException.QueryParamException
import com.sun.jersey.api.core.HttpContext
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import com.sun.jersey.server.impl.model.parameter.multivalued.{ExtractorContainerException, MultivaluedParameterExtractor}

class QueryParamInjectable(val extractor: MultivaluedParameterExtractor, decode: Boolean) extends AbstractHttpContextInjectable[Object] {

  def getValue(context: HttpContext): Object = {
    try {
      extractor.extract(context.getUriInfo.getQueryParameters(decode))
    }
    catch {
      case e: ExtractorContainerException =>
        throw new QueryParamException(e.getCause, extractor.getName, extractor.getDefaultStringValue)
    }
  }
}
