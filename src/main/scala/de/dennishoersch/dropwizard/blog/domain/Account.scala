package de.dennishoersch.dropwizard.blog.domain

import de.dennishoersch.util.hashing._

case class Account(val id: Long, val username: String, val passwordHash: String, author: Author) extends Identifiable {

  def isPasswordValid(pwd: String) = pwd.asSHA256 == passwordHash
}