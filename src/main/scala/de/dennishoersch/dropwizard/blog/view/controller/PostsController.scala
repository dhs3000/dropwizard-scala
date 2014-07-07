package de.dennishoersch.dropwizard.blog.view.controller

import javax.ws.rs.GET
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.Path
import de.dennishoersch.util.dropwizard.views.thymeleaf.ThymeleafView
import de.dennishoersch.util.resources._
import de.dennishoersch.dropwizard.blog.domain.Post
import de.dennishoersch.dropwizard.blog.service.PostsService
import scala.collection.JavaConversions._
import de.dennishoersch.dropwizard.blog.domain.Categories._
import java.util.{ Map => JavaMap, Collection => JavaCollection }
import javax.ws.rs.PathParam

@Path("/posts")
@Produces(Array(MediaType.TEXT_HTML))
class PostsController(implicit val postsService: PostsService) {

  @GET
  def all() = new PostsView(postsService.findAll)

  @GET
  @Path("author/{id}")
  def byAuthor(@PathParam("id") id: Long) = {
    val posts = postsService.findByAuthor(id)
    new PostsView(posts)
  }
  
  // Image Urls koennten bspw auch ueber einen controller/resource geladen werden statt assets, wenn sie aus der DB oder so kaemen.

  class PostsView(private val _posts: List[Post]) extends ThymeleafView("posts") {
    val posts: JavaCollection[Post] = _posts

    val categoriesOfPost: JavaMap[Long, JavaCollection[Category]] = {
      val result = _posts.map(post => (post.id, asJavaCollection(post.categories))).toMap
      result
    }
  }
}