package de.dennishoersch.dropwizard.blog.domain

import de.dennishoersch.util.hashing._

case class User(val id: Long, val name: String, val passwordHash: String) extends Identifiable {

  def isPasswordValid(pwd: String) = pwd.asSHA256 == passwordHash
}