package de.dennishoersch.dropwizard.contacts.resources

import de.dennishoersch.dropwizard.contacts.domain.Contact
import de.dennishoersch.dropwizard.contacts.domain.User
import de.dennishoersch.dropwizard.contacts.service.ContactsService
import de.dennishoersch.util.resources._
import io.dropwizard.auth.Auth
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo
import javax.ws.rs.core.MediaType

@Path("/contacts")
@Produces(Array(MediaType.APPLICATION_JSON))
class ContactsResource(implicit private val contactsService: ContactsService) {

  @GET
  def all(@Auth user: User): List[Contact] = {
    return contactsService.findAll
  }

  @GET
  @Path("{lastname}")
  def byName(@PathParam("lastname") lastname: String): Option[Contact] = {
    contactsService.findByLastname(lastname)
  }

  @GET
  @Path("view/{lastname}")
  @Produces(Array(MediaType.TEXT_HTML))
  def viewByName(@PathParam("lastname") lastname: String) = {
    new ContactView(contactsService.findByLastname(lastname).getOrElse(null))
  }
  
  @POST
  @Consumes(Array(MediaType.APPLICATION_JSON))
  def create(contact: Contact, @Context uriInfo: UriInfo): Response = {
    contactsService.save(contact)

    uriInfo.respondCreatedWith(this, contact.lastname)
  }

}