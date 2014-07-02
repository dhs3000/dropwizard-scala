package de.dennishoersch.dropwizard.contacts.domain

case class User(override val id: Long, val name: String, val pwd: String) extends Identifiable(id) {

}