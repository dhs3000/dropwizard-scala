package de.dennishoersch.util

import javax.ws.rs.core.{Response => RSResponse}

import javax.ws.rs.core.UriInfo

package object resources {

  implicit class ResonseWithUri(val uriInfo: UriInfo) {
    def respondCreatedWith(mainResource: AnyRef, subpath: String)
    	= RSResponse.created(uriInfo.getBaseUriBuilder().path(mainResource.getClass).path(subpath).build()).build()

    def seeOther(resourceClass: Class[_])
    	= RSResponse.seeOther(uriInfo.getBaseUriBuilder().path(resourceClass).build()).build()
  }
}