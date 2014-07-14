package de.dennishoersch.util.json;

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
