package de.dennishoersch.dropwizard.blog.domain

object Category extends Enumeration {
  type Category = Value
  val CSS, Pure, Javascript, Uncategorized = Value
}