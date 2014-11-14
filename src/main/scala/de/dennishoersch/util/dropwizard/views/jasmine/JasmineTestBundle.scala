package de.dennishoersch.util.dropwizard.views.jasmine

import de.dennishoersch.util.dropwizard.assets.AssetsBundle
import io.dropwizard.Bundle
import io.dropwizard.setup.{Bootstrap, Environment}

object JasmineTestBundle extends Bundle {

  override def initialize(bootstrap: Bootstrap[_]) {
    bootstrap.addBundle(
      AssetsBundle(
        name = "specs",
        resourcePath = "/view/specs",
        uriPath = "/specs",
        indexFile = "index.html",
        caching = false))
  }

  override def run(environment: Environment) {
  }
}
