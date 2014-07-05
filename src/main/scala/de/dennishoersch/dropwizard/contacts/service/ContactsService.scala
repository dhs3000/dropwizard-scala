package de.dennishoersch.dropwizard.contacts.service

import de.dennishoersch.dropwizard.contacts.domain.Contact
import scala.collection.mutable

class ContactsService {

  private val contacts: mutable.ArrayBuffer[Contact] = mutable.ArrayBuffer()

  def findAll(): List[Contact] = contacts.toList

  def findByLastname(lastname: String): Option[Contact] = contacts.find(contact => contact.lastname == lastname)
  
  def save(contact: Contact) = {
    contacts += contact
  }

}