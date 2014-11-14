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
package de.dennishoersch.util.json

import scala.util.Success
import org.junit.Test
import org.scalatest.Matchers
import org.scalatest.junit.AssertionsForJUnit
import de.dennishoersch.dropwizard.blog.domain.Account
import de.dennishoersch.util.test.CustomMatchers
import de.dennishoersch.dropwizard.blog.domain.Author

class JsonTest extends AssertionsForJUnit with Matchers with CustomMatchers {

  val json = new Json

  @Test
  def test_simple_stringify() {
    val obj = Account(2, "Denn", "Mang", Author(1, "", ""))
    val expected = """{
      |  "name" : "Denn",
      |  "pwd" : "Mang"
      |}""".stripMargin

    val stringified = json.stringify(obj)

    stringified should be (anInstanceOf[Success[String]])
    
    stringified.get should equal (expected)

  }
}
