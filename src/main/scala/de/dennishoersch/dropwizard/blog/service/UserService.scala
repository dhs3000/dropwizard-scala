package de.dennishoersch.dropwizard.blog.service

import de.dennishoersch.util.hashing._
import de.dennishoersch.dropwizard.blog.domain.User

class UserService {

  private val users = List(User(1, "dennis", "secret".asSHA256))

  def findByName(name: String):Option[User] = {
    users.find(_.name == name)
  }
}