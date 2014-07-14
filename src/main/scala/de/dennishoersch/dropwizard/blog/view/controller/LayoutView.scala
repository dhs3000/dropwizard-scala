package de.dennishoersch.dropwizard.blog.view.controller

import java.util.{ Map => JavaMap, Collection => JavaCollection }
import scala.collection.JavaConversions._
import de.dennishoersch.dropwizard.blog.service.PostsService
import de.dennishoersch.dropwizard.blog.domain.Author
import de.dennishoersch.dropwizard.blog.domain.Category._

/**
 * Defines Values used in the general layout.
 */
trait LayoutView {
	protected val postsService: PostsService
	
    val allAuthors: JavaCollection[Author] = postsService.findAllAuthors
    val allCategories: JavaCollection[Category] = postsService.findAllCategories
}