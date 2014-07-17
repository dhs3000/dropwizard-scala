package de.dennishoersch.dropwizard.blog.view.controller

import java.util.{ Map => JavaMap, Collection => JavaCollection }
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import scala.collection.JavaConversions._
import io.dropwizard.auth.Auth
import de.dennishoersch.dropwizard.blog.domain.Account
import de.dennishoersch.dropwizard.blog.domain.Author
import de.dennishoersch.dropwizard.blog.domain.Category.Category
import de.dennishoersch.dropwizard.blog.domain.Post
import de.dennishoersch.dropwizard.blog.service.PostsService
import de.dennishoersch.util.dropwizard.views.thymeleaf.ThymeleafView
import de.dennishoersch.util.resources._
import javax.ws.rs.POST
import javax.ws.rs.Consumes
import javax.ws.rs.FormParam
import javax.ws.rs.core.UriInfo
import javax.ws.rs.core.Context
import de.dennishoersch.util.resources.Response

@Path("/posts")
@Produces(Array(MediaType.TEXT_HTML))
class PostsController(implicit val postsService: PostsService) {

  // Image Urls koennten bspw auch ueber einen controller/resource geladen werden statt assets, wenn sie aus der DB oder so kaemen.

  @GET
  def all() = new PostsView(postsService.findAll)

  @GET
  @Path("{id}")
  def byId(@PathParam("id") id: Long) = {
    val post = postsService.findById(id)
    new PostsByAuthorView(post.map(post => post.author.name).getOrElse(""), post.toList)
  }

  @GET
  @Path("author/{id}")
  def byAuthor(@PathParam("id") id: Long) = {
    val posts = postsService.findByAuthor(id)
    new PostsByAuthorView(posts.headOption.map(post => post.author.name).getOrElse(""), posts)
  }

  @GET
  @Path("category/{name}")
  def byCategory(@PathParam("name") name: String) = {
    val posts = postsService.findByCategory(name)
    new PostsByCategoryView(name, posts)
  }

  @GET
  @Path("create-new")
  def createNew(@Auth account: Account) = {
    new CreateNewPostView(account.author)
  }

  @POST
  @Consumes(Array(MediaType.APPLICATION_FORM_URLENCODED))
  def createNew(@Auth account: Account, @FormParam("title") title: String, @FormParam("content") content: String, @Context uriInfo: UriInfo) = {
    // TODO: Let the user select the categories
    // TODO: @BeanParam is part of JAX-RS 2, that might be part of Dropwizard 0.8

    val post = postsService.createPost(account.author, title, content)
    Response.redirectGet(uriInfo, this, post.id)
  }

  class PostsByAuthorView(val authorName: String, posts: List[Post]) extends PostsView(posts) {
    val authorView = true
  }

  class PostsByCategoryView(val categoryName: String, posts: List[Post]) extends PostsView(posts) {
    val categoryView = true
  }

  class PostsView(private val _posts: List[Post])(implicit protected val postsService: PostsService)
    extends ThymeleafView("posts")
    with LayoutView {
    
    val posts: JavaCollection[Post] = _posts

    val categoriesOfPost: JavaMap[Long, JavaCollection[Category]] = {
      val result = _posts.map(post => (post.id, asJavaCollection(post.categories))).toMap
      result
    }
  }

  class CreateNewPostView(private val author: Author)(implicit protected val postsService: PostsService)
    extends ThymeleafView("create-new-post")
    with LayoutView
}