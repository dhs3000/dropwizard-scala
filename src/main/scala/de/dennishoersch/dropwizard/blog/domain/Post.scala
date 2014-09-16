package de.dennishoersch.dropwizard.blog.domain

import Category._
import org.joda.time.LocalDateTime

case class Post(id: Long, author: Author, date: LocalDateTime, title: String, content: String, categories: Iterable[Category]) extends Identifiable {

}