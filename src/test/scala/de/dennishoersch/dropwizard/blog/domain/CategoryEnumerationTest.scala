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
package de.dennishoersch.dropwizard.blog.domain

import org.junit.Test
import org.scalatest.Matchers
import org.scalatest.junit.AssertionsForJUnit

import de.dennishoersch.dropwizard.blog.domain.Category._
import de.dennishoersch.util.test.CustomMatchers

class CategoryEnumerationTest extends AssertionsForJUnit with Matchers with CustomMatchers {

  @Test
  def test_category_name() {
    val obj = Pure
    assert(obj == Category.withName("Pure"))
    assert(obj.toString == "Pure")

  }
}
