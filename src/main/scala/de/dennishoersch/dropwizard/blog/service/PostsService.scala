/*!
 * Copyright 2013-2014 Dennis Hörsch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

  def findAllAuthors: Set[Author] = posts.map(_.author).toSet

  def findAllCategories: Set[Category] = posts.flatMap(_.categories).toSet

  
  def createPost(author: Author, title: String, content: String): Post
    = createPost(author, title, content, Uncategorized)
  
  def createPost(author: Author, title: String, content: String, category: Category): Post
    = createPost(author, title, content, Seq(category))
  
  def createPost(author: Author, title: String, content: String, categories: Seq[Category]): Post = {
    db.savePost(Post(db.nextId(), author, LocalDateTime.now, title, content, categories))
  }
  
}