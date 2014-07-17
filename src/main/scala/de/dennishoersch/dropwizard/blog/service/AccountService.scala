package de.dennishoersch.dropwizard.blog.service

import de.dennishoersch.dropwizard.blog.domain.Account

import de.dennishoersch.util.hashing._

class AccountService(implicit val db: DB) {

  private val accounts = db.allAccounts()

  def findByUsername(name: String): Option[Account] = {
    accounts.find(_.username == name)
  }
}