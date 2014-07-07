package de.dennishoersch.dropwizard.blog.domain

case class Author(id: Long, name: String, avatarUrl: String) extends Identifiable {

}