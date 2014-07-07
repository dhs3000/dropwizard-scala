package de.dennishoersch.dropwizard.blog.domain

object Categories {

  // macro?

  trait Category {
    val name = toString
  }

  case object Uncategorized extends Category
  
  case object CSS extends Category
  case object Pure extends Category
  case object Javascript extends Category

}