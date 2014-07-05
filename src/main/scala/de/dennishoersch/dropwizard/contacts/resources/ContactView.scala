package de.dennishoersch.dropwizard.contacts.resources

import io.dropwizard.views.View
import de.dennishoersch.dropwizard.contacts.domain.Contact
import de.dennishoersch.util.dropwizard.views.thymeleaf.ThymeleafView

class ContactView(val contact: Contact) extends ThymeleafView("contact") {
  val firstname = contact.firstname
}