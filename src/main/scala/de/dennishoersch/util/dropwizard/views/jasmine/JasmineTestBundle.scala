package de.dennishoersch.util.dropwizard.views.jasmine

import de.dennishoersch.util.dropwizard.views.thymeleaf.{DefaultThymeleafViewRenderer, ScalaObjectPropertyAccessor}
import io.dropwizard.Bundle
import io.dropwizard.assets.AssetsBundle
import io.dropwizard.setup.{Environment, Bootstrap}
import io.dropwizard.views.ViewBundle
import ognl.OgnlRuntime

object JasmineTestBundle  extends Bundle {

  override def initialize(bootstrap: Bootstrap[_]) {
    bootstrap.addBundle(new AssetsBundle("/view/specs", "/specs", "index.html", "specs"))
  }

  override def run(environment: Environment) {
  }
}
