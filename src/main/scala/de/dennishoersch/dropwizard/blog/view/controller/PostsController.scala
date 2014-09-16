package de.dennishoersch.dropwizard.blog.view.controller

import javax.ws.rs._
import javax.ws.rs.core.{UriInfo, Context, MediaType}
import java.util.{Collection => JavaCollection, Map => JavaMap}

import de.dennishoersch.dropwizard.blog.domain.Category.Category
import de.dennishoersch.dropwizard.blog.domain.{Category, Account, Author, Post}
import de.dennishoersch.dropwizard.blog.service.PostsService
import de.dennishoersch.util.dropwizard.views.thymeleaf.ThymeleafView
import de.dennishoersch.util.resources.Response
import io.dropwizard.auth.Auth

import scala.collection.JavaConversions._

@Path("/posts")
@Produces(Array(MediaType.TEXT_HTML))
class PostsController(implicit val postsService: PostsService) {

  // Image Urls koennten bspw auch ueber einen controller/resource geladen werden statt assets,
  // wenn sie aus der DB oder so kaemen.

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
  def createNew(@Auth account: Account,
                @FormParam("title") title: String,
                @FormParam("content") content: String,
                @FormParam("categories") categories: Seq[String],
                @Context uriInfo: UriInfo) = {

    // TODO: Let the user select the categories
    // TODO: @BeanParam is part of JAX-RS 2, that might be part of Dropwizard 0.8

    // TODO Scala Seq erlauben wie Option 

    val post = postsService.createPost(account.author, title, content, categories.map(c => Category.withName(c)))
    Response.redirectGet(uriInfo, this, post.id)
  }

  class PostsByAuthorView(val authorName: String, posts: Seq[Post]) extends PostsView(posts) {
    val authorView = true
  }

  class PostsByCategoryView(val categoryName: String, posts: Seq[Post]) extends PostsView(posts) {
    val categoryView = true
  }

  class PostsView(private val _posts: Seq[Post])(implicit protected val postsService: PostsService)
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
    with LayoutView {

    val categories: JavaCollection[Category] = postsService.findAllCategories
  }

}