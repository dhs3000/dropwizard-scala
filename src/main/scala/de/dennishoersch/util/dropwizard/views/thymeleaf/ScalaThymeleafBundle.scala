package de.dennishoersch.util.dropwizard.views.thymeleaf

import io.dropwizard.setup.Environment
import io.dropwizard.Bundle
import io.dropwizard.setup.Bootstrap
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import io.dropwizard.views.ViewBundle
import scala.collection.JavaConversions._
import ognl.OgnlRuntime
import io.dropwizard.assets.AssetsBundle

object ScalaThymeleafBundle extends ScalaThymeleafBundle

class ScalaThymeleafBundle extends Bundle {
  // Using ConfiguredBundle?

  override def initialize(bootstrap: Bootstrap[_]) {
    OgnlRuntime.setPropertyAccessor(classOf[Object], new ScalaObjectPropertyAccessor)

    bootstrap.addBundle(new AssetsBundle("/view/css", "/css", null, "css"))
    bootstrap.addBundle(new AssetsBundle("/view/images", "/img", null, "images"))

    bootstrap.addBundle(new ViewBundle(Seq(DefaultThymeleafViewRenderer)))
  }

  override def run(environment: Environment) {
  }
}