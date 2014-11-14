package de.dennishoersch.util.dropwizard.assets

import io.dropwizard.Bundle
import io.dropwizard.setup.{Environment, Bootstrap}

class AssetsBundle private(
  name: String,
  resourcePath: String,
  uriPath: String,
  indexFile: Option[String] = None,
  caching: Boolean = true)
  extends Bundle {

  override def initialize(bootstrap: Bootstrap[_]) {
  }

  override def run(environment: Environment) {
    environment.servlets
      .addServlet(name, new AssetServlet(resourcePath, uriPath, indexFile, caching))
      .addMapping(uriPath + '*')
  }
}

object AssetsBundle {

  def apply(name: String, resourcePath: String, uriPath: String): AssetsBundle =
    apply(name, resourcePath, uriPath, None, caching = true)

  def apply(name: String, resourcePath: String, uriPath: String, indexFile: String, caching: Boolean): AssetsBundle =
    apply(name, resourcePath, uriPath, Some(indexFile), caching)

  def apply(name: String, resourcePath: String, uriPath: String, indexFile: Option[String], caching: Boolean): AssetsBundle =
    new AssetsBundle(name, addSlash(resourcePath), addSlash(uriPath), indexFile, caching)

  private def addSlash(path: String) = if (path.endsWith("/")) path else path + '/'
}
