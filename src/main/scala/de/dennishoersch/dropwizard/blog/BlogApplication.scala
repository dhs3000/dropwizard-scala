package de.dennishoersch.dropwizard.blog

import de.dennishoersch.dropwizard.blog.auth.UserAuthenticator
import de.dennishoersch.dropwizard.blog.service.UserService
import de.dennishoersch.dropwizard.blog.view.controller.IndexController
import de.dennishoersch.dropwizard.blog.view.controller.PostsController
import de.dennishoersch.util.dropwizard.ScalaApplication
import de.dennishoersch.util.dropwizard.ScalaBundle
import de.dennishoersch.util.dropwizard.config.RootClasspathConfigurationSourceProvider
import de.dennishoersch.util.dropwizard.views.thymeleaf.ScalaThymeleafBundle
import io.dropwizard.auth.basic.BasicAuthProvider
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import de.dennishoersch.dropwizard.blog.service.PostsService

object BlogApplication extends ScalaApplication[BlogConfiguration] {

  override def getName() = "blog-application"

  override def initialize(bootstrap: Bootstrap[BlogConfiguration]) = {
    bootstrap.setConfigurationSourceProvider(RootClasspathConfigurationSourceProvider)
    bootstrap.addBundle(ScalaBundle)
    bootstrap.addBundle(ScalaThymeleafBundle)
  }

  override def run(configuration: BlogConfiguration, environment: Environment) = {

    implicit val userService = new UserService
    implicit val postsService = new PostsService
    
    
    
    environment.jersey().register(new BasicAuthProvider(new UserAuthenticator, "SUPER SECRET STUFF"))

    environment.jersey().register(new IndexController)
    environment.jersey().register(new PostsController)
  }


}