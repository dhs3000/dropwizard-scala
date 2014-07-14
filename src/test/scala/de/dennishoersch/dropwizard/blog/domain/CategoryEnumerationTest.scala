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
