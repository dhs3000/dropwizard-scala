package de.dennishoersch.dropwizard.contacts

import io.dropwizard.auth.basic.BasicCredentials
import de.dennishoersch.dropwizard.contacts.domain.User
import de.dennishoersch.dropwizard.util.dropwizard.Authenticator

class UserAuthenticator extends Authenticator[BasicCredentials, User] {
  override def auth(credentials: BasicCredentials): Option[User] = {
    if ("secret".equals(credentials.getPassword())) {
      return Some(User(1, credentials.getUsername, credentials.getPassword))
    }
    return None
  }
}