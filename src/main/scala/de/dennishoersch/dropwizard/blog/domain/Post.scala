package de.dennishoersch.dropwizard.blog.domain

import java.time.LocalDateTime
import Categories.Category

case class Post(id: Long, author: Author, date: LocalDateTime, title: String, content: String, categories: Iterable[Category]) extends Identifiable {

}