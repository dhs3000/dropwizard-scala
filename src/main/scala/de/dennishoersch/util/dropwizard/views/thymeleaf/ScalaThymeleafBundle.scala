package de.dennishoersch.util.dropwizard.views.thymeleaf

import scala.collection.JavaConversions._

import io.dropwizard.Bundle
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.Bootstrap

import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle

import ognl.OgnlRuntime

object ScalaThymeleafBundle extends ScalaThymeleafBundle

class ScalaThymeleafBundle extends Bundle {

  override def initialize(bootstrap: Bootstrap[_]) {
    OgnlRuntime.setPropertyAccessor(classOf[Object], new ScalaObjectPropertyAccessor)

    bootstrap.addBundle(new AssetsBundle("/view/css", "/css", null, "css"))
    bootstrap.addBundle(new AssetsBundle("/view/js", "/js", null, "js"))
    bootstrap.addBundle(new AssetsBundle("/view/images", "/img", null, "images"))

    bootstrap.addBundle(new ViewBundle(Seq(DefaultThymeleafViewRenderer)))
  }

  override def run(environment: Environment) {
  }
}