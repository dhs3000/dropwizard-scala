package de.dennishoersch.dropwizard.blog.auth

import io.dropwizard.auth.basic.BasicCredentials

import de.dennishoersch.dropwizard.blog.domain.Account
import de.dennishoersch.dropwizard.blog.service.AccountService
import de.dennishoersch.util.dropwizard.auth.Authenticator

class AccountAuthenticator(implicit accountService: AccountService) extends Authenticator[BasicCredentials, Account] {

  override def auth(credentials: BasicCredentials): Option[Account] = {
    accountService.findByUsername(credentials.getUsername())
      .filter(_.isPasswordValid(credentials.getPassword()))
  }
}