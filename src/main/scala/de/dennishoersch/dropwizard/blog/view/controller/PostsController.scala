package de.dennishoersch.dropwizard.blog.view.controller

import java.util.{ Map => JavaMap, Collection => JavaCollection }

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

import scala.collection.JavaConversions._

import de.dennishoersch.dropwizard.blog.domain.Author
import de.dennishoersch.dropwizard.blog.domain.Categories._
import de.dennishoersch.dropwizard.blog.domain.Post
import de.dennishoersch.dropwizard.blog.service.PostsService
import de.dennishoersch.util.dropwizard.views.thymeleaf.ThymeleafView
import de.dennishoersch.util.resources._

@Path("/posts")
@Produces(Array(MediaType.TEXT_HTML))
class PostsController(implicit val postsService: PostsService) {

  // Image Urls koennten bspw auch ueber einen controller/resource geladen werden statt assets, wenn sie aus der DB oder so kaemen.

  @GET
  def all() = new PostsView(postsService.findAll)

  @GET
  @Path("author/{id}")
  def byAuthor(@PathParam("id") id: Long) = {
    val posts = postsService.findByAuthor(id)
    // FIXME: wenn leer dann kein Author da!
    new PostsByAuthorView(posts.head.author.name, posts)
  }

  @GET
  @Path("category/{name}")
  def byCategory(@PathParam("name") name: String) = {
    val posts = postsService.findByCategory(name)
    new PostsByCategoryView(name, posts)
  }

  class PostsByAuthorView(val authorName: String, posts: List[Post]) extends PostsView(posts) {
    val authorView = true
  }

  class PostsByCategoryView(val categoryName: String, posts: List[Post]) extends PostsView(posts) {
    val categoryView = true
  }

  class PostsView(private val _posts: List[Post]) extends ThymeleafView("posts") {
    val posts: JavaCollection[Post] = _posts

    val categoriesOfPost: JavaMap[Long, JavaCollection[Category]] = {
      val result = _posts.map(post => (post.id, asJavaCollection(post.categories))).toMap
      result
    }

    val allAuthors: JavaCollection[Author] = postsService.findAllAuthors
    val allCategories: JavaCollection[Category] = postsService.findAllCategories

  }
}