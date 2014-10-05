package de.dennishoersch.dropwizard.blog

import de.dennishoersch.dropwizard.blog.auth.AccountAuthenticator
import de.dennishoersch.dropwizard.blog.service.{AccountService, DB, PostsService}
import de.dennishoersch.dropwizard.blog.view.controller.{IndexController, PostsController}
import de.dennishoersch.util.dropwizard.config.RootClasspathConfigurationSourceProvider
import de.dennishoersch.util.dropwizard.views.jasmine.JasmineTestBundle
import de.dennishoersch.util.dropwizard.views.thymeleaf.ScalaThymeleafBundle
import de.dennishoersch.util.dropwizard.{ScalaApplication, ScalaBundle}
import de.dennishoersch.util.jersey.inject.{OptionFormParamInjectableProvider, OptionQueryParamInjectableProvider, SeqFormParamInjectableProvider, SeqQueryParamInjectableProvider}
import io.dropwizard.auth.basic.BasicAuthProvider
import io.dropwizard.setup.{Bootstrap, Environment}

object BlogApplication extends ScalaApplication[BlogConfiguration] {

  override def getName = "blog-application"

  override def initialize(bootstrap: Bootstrap[BlogConfiguration]) = {
    bootstrap.setConfigurationSourceProvider(RootClasspathConfigurationSourceProvider)
    bootstrap.addBundle(ScalaBundle)
    bootstrap.addBundle(ScalaThymeleafBundle)

    bootstrap.addBundle(JasmineTestBundle)
  }

  override def run(configuration: BlogConfiguration, environment: Environment) = {
    registerParameterInjectableProvider(environment)

    implicit val db = new DB
    implicit val userService = new AccountService
    implicit val postsService = new PostsService

    environment.jersey.register(new BasicAuthProvider(new AccountAuthenticator, "SUPER SECRET STUFF"))

    environment.jersey.register(new IndexController)
    environment.jersey.register(new PostsController)
  }

  private def registerParameterInjectableProvider(environment: Environment) = {
    for {
      provider <- Seq(
        classOf[OptionQueryParamInjectableProvider],
        classOf[OptionFormParamInjectableProvider],
        classOf[SeqFormParamInjectableProvider])
    } environment.jersey.register(provider)
  }
}