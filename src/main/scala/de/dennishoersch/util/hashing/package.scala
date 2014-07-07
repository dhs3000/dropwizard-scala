package de.dennishoersch.util

import java.security.MessageDigest
package object hashing {

  object Hasher {
    private def sha256Of(str: String): String = {
      val digester = MessageDigest.getInstance("SHA-256")
      digester.update(str.getBytes("UTF-8"))
      new String(digester.digest())
    }
  }

  implicit class Hasher(str: String) {
    def asSHA256 = Hasher.sha256Of(str)
  }

}