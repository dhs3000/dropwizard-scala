package de.dennishoersch.util.json;

import scala.util.Success
import org.junit.Test
import org.scalatest.Matchers
import org.scalatest.junit.AssertionsForJUnit
import de.dennishoersch.dropwizard.blog.domain.User
import de.dennishoersch.util.test.CustomMatchers

class JsonTest extends AssertionsForJUnit with Matchers with CustomMatchers {

  val json = new Json

  @Test
  def test_simple_stringify() {
    val obj = User(2, "Denn", "Mang")
    val expected = """{
      |  "name" : "Denn",
      |  "pwd" : "Mang"
      |}""".stripMargin

    val stringified = json.stringify(obj)

    stringified should be (anInstanceOf[Success[String]])
    
    stringified.get should equal (expected)

  }
}
