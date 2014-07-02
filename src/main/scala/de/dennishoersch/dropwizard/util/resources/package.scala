package de.dennishoersch.dropwizard.util

import javax.ws.rs.core.UriInfo
import javax.ws.rs.core.Response

package object resources {

  implicit class ResonseWithUri(val uriInfo: UriInfo) {
    def respondCreatedWith(mainResource: AnyRef, subpath: String)
    	= Response.created(uriInfo.getBaseUriBuilder().path(mainResource.getClass).path(subpath).build()).build()
  }
}