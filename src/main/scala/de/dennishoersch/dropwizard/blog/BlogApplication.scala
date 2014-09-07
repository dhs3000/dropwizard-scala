package de.dennishoersch.dropwizard.blog

import de.dennishoersch.util.dropwizard.{ScalaApplication, ScalaBundle}
import de.dennishoersch.util.dropwizard.config.RootClasspathConfigurationSourceProvider
import de.dennishoersch.util.dropwizard.views.thymeleaf.ScalaThymeleafBundle
import de.dennishoersch.util.jersey.inject.{OptionFormParamInjectableProvider, OptionQueryParamInjectableProvider}
import io.dropwizard.setup.{Bootstrap, Environment}

object BlogApplication extends ScalaApplication[BlogConfiguration] {

  override def getName = "blog-application"

  override def initialize(bootstrap: Bootstrap[BlogConfiguration]) = {
    bootstrap.setConfigurationSourceProvider(RootClasspathConfigurationSourceProvider)
    bootstrap.addBundle(ScalaBundle)
    bootstrap.addBundle(ScalaThymeleafBundle)
  }

  override def run(configuration: BlogConfiguration, environment: Environment) = {
    for {
      provider <- Seq(
        classOf[OptionQueryParamInjectableProvider],
        classOf[OptionFormParamInjectableProvider])
    } environment.jersey.register(provider)

    implicit val db = new DB
    implicit val userService = new AccountService
    implicit val postsService = new PostsService

    environment.jersey.register(new BasicAuthProvider(new AccountAuthenticator, "SUPER SECRET STUFF"))

    environment.jersey.register(new IndexController)
    environment.jersey.register(new PostsController)
  }

}