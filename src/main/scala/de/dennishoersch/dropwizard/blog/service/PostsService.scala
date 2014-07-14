package de.dennishoersch.dropwizard.blog.service

import de.dennishoersch.util.time._

class PostsService(implicit val db: DB) {

  val posts = db.allPosts()

  def findAll() = posts

  def findByAuthor(id: Long) = posts.filter(_.author.id == id)

  def findByCategory(name: String) = posts.filter(_.categories.exists(_.toString == name))

  def findAllAuthors() = posts.map(_.author).toSet

  def findAllCategories() = posts.flatMap(_.categories).toSet

}