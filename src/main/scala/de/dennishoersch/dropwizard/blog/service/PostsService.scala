package de.dennishoersch.dropwizard.blog.service

import java.time.LocalDateTime

import de.dennishoersch.dropwizard.blog.domain.Author
import de.dennishoersch.dropwizard.blog.domain.Category._
import de.dennishoersch.dropwizard.blog.domain.Post

import de.dennishoersch.util.time._

class PostsService(implicit val db: DB) {

  private def posts = db.allPosts()

  def findAll(): List[Post] = posts

  def findById(id: Long): Option[Post] = posts.find(_.id == id)

  def findByAuthor(id: Long): List[Post] = posts.filter(_.author.id == id)

  def findByCategory(name: String): List[Post] = posts.filter(_.categories.exists(_.toString == name))

  def findAllAuthors(): Set[Author] = posts.map(_.author).toSet

  def findAllCategories(): Set[Category] = posts.flatMap(_.categories).toSet

  def createPost(author: Author, title: String, content: String): Post = {
    db.savePost(Post(db.nextId, author, LocalDateTime.now, title, content, List(Uncategorized)))
  }
}