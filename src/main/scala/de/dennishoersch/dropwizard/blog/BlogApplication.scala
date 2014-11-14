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