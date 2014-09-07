package de.dennishoersch.util.jersey.inject

import javax.ws.rs.core.MediaType

import com.sun.jersey.api.ParamException.FormParamException
import com.sun.jersey.api.core.{ HttpContext, HttpRequestContext }
import com.sun.jersey.api.representation.Form
import com.sun.jersey.core.header.MediaTypes
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable
import com.sun.jersey.server.impl.model.method.dispatch.FormDispatchProvider
import com.sun.jersey.server.impl.model.parameter.multivalued.{ ExtractorContainerException, MultivaluedParameterExtractor }

class FormParamInjectable(val extractor: MultivaluedParameterExtractor) extends AbstractHttpContextInjectable[Object] {

  def getValue(context: HttpContext): Object = {
    try {
      extractor.extract(form(context))
    }
    catch {
      case e: ExtractorContainerException =>
        throw new FormParamException(e.getCause, extractor.getName, extractor.getDefaultStringValue)
    }
  }

  private def form(context: HttpContext) = {
    val form = getCachedForm(context);
    if (form.isDefined) {
      form.get
    }
    else {
      cacheForm(context, getUncachedForm(context))
    }
  }

  private def getCachedForm(context: HttpContext): Option[Form] = {
    Option(context.getProperties.get(FormDispatchProvider.FORM_PROPERTY).asInstanceOf[Form])
  }

  private def cacheForm(context: HttpContext, form: Form): Form = {
    context.getProperties().put(FormDispatchProvider.FORM_PROPERTY, form)
    form
  }

  private def getUncachedForm(context: HttpContext): Form = {
    val r = ensureValidRequest(context.getRequest)
    r.getFormParameters
  }

  private def ensureValidRequest(r: HttpRequestContext): HttpRequestContext = {
    if (r.getMethod.equals("GET")) {
      throw new IllegalStateException(
        "The @FormParam is utilized when the request method is GET");
    }

    if (!MediaTypes.typeEquals(MediaType.APPLICATION_FORM_URLENCODED_TYPE, r.getMediaType)) {
      throw new IllegalStateException(
        "The @FormParam is utilized when the content type of the request entity "
          + "is not application/x-www-form-urlencoded");
    }

    r
  }

}
