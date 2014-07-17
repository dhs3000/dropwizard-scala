package de.dennishoersch.util.resources

import javax.ws.rs.core.{Response => RSResponse}
import javax.ws.rs.core.UriInfo

object Response {
  def redirectGet(uriInfo: UriInfo, mainResource: AnyRef, subpath: Any)
  	= RSResponse.seeOther(
  	    uriInfo.getBaseUriBuilder().path(mainResource.getClass).path(subpath.toString).build()
  	  ).build()
}