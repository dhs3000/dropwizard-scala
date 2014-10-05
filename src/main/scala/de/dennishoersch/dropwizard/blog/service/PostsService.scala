package de.dennishoersch.dropwizard.blog.service


import de.dennishoersch.dropwizard.blog.domain.Category._
import de.dennishoersch.dropwizard.blog.domain.{Author, Post}
import org.joda.time.LocalDateTime

class PostsService(implicit val db: DB) {

  private def posts = db.allPosts()

  def findAll(): Seq[Post] = posts

  def findById(id: Long): Option[Post] = posts.find(_.id == id)

  def findByAuthor(id: Long): Seq[Post] = posts.filter(_.author.id == id)

  def findByCategory(name: String): Seq[Post] = posts.filter(_.categories.exists(_.toString == name))

  def findAllAuthors(): Set[Author] = posts.map(_.author).toSet

  def findAllCategories(): Set[Category] = posts.flatMap(_.categories).toSet

  
  def createPost(author: Author, title: String, content: String): Post
    = createPost(author, title, content, Uncategorized)
  
  def createPost(author: Author, title: String, content: String, category: Category): Post
    = createPost(author, title, content, Seq(category))
  
  def createPost(author: Author, title: String, content: String, categories: Seq[Category]): Post = {
    db.savePost(Post(db.nextId, author, LocalDateTime.now, title, content, categories))
  }
  
}