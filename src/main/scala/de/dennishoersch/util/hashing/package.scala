/*!
 * Copyright 2013-2014 Dennis Hörsch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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