package de.dennishoersch.dropwizard.blog.auth

import io.dropwizard.auth.basic.BasicCredentials

import de.dennishoersch.dropwizard.blog.domain.User
import de.dennishoersch.dropwizard.blog.service.UserService
import de.dennishoersch.util.dropwizard.auth.Authenticator

class UserAuthenticator(implicit userService: UserService) extends Authenticator[BasicCredentials, User] {
  
  override def auth(credentials: BasicCredentials): Option[User] = {
    userService.findByName(credentials.getUsername())
      .filter(_.isPasswordValid(credentials.getPassword()))
  }
}