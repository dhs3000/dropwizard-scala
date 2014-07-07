package de.dennishoersch.dropwizard.blog.view.controller

import javax.ws.rs.Path
import javax.ws.rs.GET
import javax.ws.rs.core.UriInfo
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import de.dennishoersch.util.resources._

@Path("/")
class IndexController {

  @GET
  def showPosts(@Context uriInfo: UriInfo) = uriInfo.seeOther(classOf[PostsController])
}