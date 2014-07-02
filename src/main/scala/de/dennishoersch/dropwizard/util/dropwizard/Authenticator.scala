package de.dennishoersch.dropwizard.util.dropwizard

import de.dennishoersch.dropwizard.util.guava._

trait Authenticator[C, P] extends io.dropwizard.auth.Authenticator[C, P] {

  final override def authenticate(credentials: C) = auth(credentials)

  def auth(credentials: C): Option[P]
}