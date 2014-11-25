package de.dennishoersch.util.dropwizard.views.thymeleaf

import de.dennishoersch.util.assets.AssetsBundle
import de.dennishoersch.util.dropwizard.config.{DeploymentConfiguration, ConfiguredBundle}

import scala.collection.JavaConversions._

import io.dropwizard.Bundle
import io.dropwizard.setup.Bootstrap

import io.dropwizard.setup.Environment
import io.dropwizard.views.ViewBundle

import ognl.OgnlRuntime

object ScalaThymeleafBundle extends ScalaThymeleafBundle

class ScalaThymeleafBundle extends ConfiguredBundle[DeploymentConfiguration] {

  override def init(bootstrap: Bootstrap[DeploymentConfiguration]) {
    OgnlRuntime.setPropertyAccessor(classOf[Object], new ScalaObjectPropertyAccessor)

    for (assetsBundle <- Seq(
      AssetsBundle(
        name = "css",
        resourcePath = "/view/css",
        uriPath = "/css"),
      AssetsBundle(
        name = "js",
        resourcePath = "/view/js",
        uriPath = "/js"),
      AssetsBundle(
        name = "images",
        resourcePath = "/view/images",
        uriPath = "/images"))) {
      bootstrap.addBundle(assetsBundle)
    }

    bootstrap.addBundle(new ViewBundle(Seq(DefaultThymeleafViewRenderer)))
  }

  override def run(configuration: DeploymentConfiguration, environment: Environment) {
  }
}