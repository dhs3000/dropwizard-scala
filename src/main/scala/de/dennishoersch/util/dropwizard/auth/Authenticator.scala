package de.dennishoersch.util.dropwizard.auth

import de.dennishoersch.util.guava._

trait Authenticator[C, P] extends io.dropwizard.auth.Authenticator[C, P] {

  final override def authenticate(credentials: C) = auth(credentials)

  def auth(credentials: C): Option[P]
}