package de.dennishoersch.dropwizard.contacts

import de.dennishoersch.dropwizard.contacts.domain.Contact
import de.dennishoersch.dropwizard.contacts.resources.ContactsResource
import de.dennishoersch.dropwizard.contacts.service.ContactsService
import de.dennishoersch.dropwizard.util.dropwizard.ClassRelativeConfigurationSourceProvider
import de.dennishoersch.dropwizard.util.dropwizard.ScalaApplication
import io.dropwizard.auth.basic.BasicAuthProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import de.dennishoersch.dropwizard.contacts.domain.User
import de.dennishoersch.dropwizard.util.dropwizard.ScalaBundle
import de.dennishoersch.dropwizard.util.dropwizard.RootClasspathConfigurationSourceProvider

object ContactsApplication extends ScalaApplication[ContactsConfiguration] {

  override def getName() = "contacts-application"

  override def initialize(bootstrap: Bootstrap[ContactsConfiguration]) = {
    bootstrap.setConfigurationSourceProvider(RootClasspathConfigurationSourceProvider)
    bootstrap.addBundle(ScalaBundle)
  }

  override def run(configuration: ContactsConfiguration, environment: Environment) = {

    environment.jersey().register(new BasicAuthProvider[User](new UserAuthenticator(), "SUPER SECRET STUFF"))

    implicit val contactsService = new ContactsService
    setUpContacts(contactsService)

    val contactsResource = new ContactsResource
    environment.jersey().register(contactsResource)
  }

  private def setUpContacts(contactsService: ContactsService) = {
    contactsService.save(Contact("Nummer", "Eins"))
    contactsService.save(Contact("Nummer", "Zweieinhalb"))
  }

}