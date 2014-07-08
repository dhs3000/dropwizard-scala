package de.dennishoersch.util.dropwizard

import io.dropwizard.Bundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment

import com.fasterxml.jackson.module.scala.DefaultScalaModule

object ScalaBundle extends ScalaBundle

class ScalaBundle extends Bundle {
  override def initialize(bootstrap: Bootstrap[_]) {
    bootstrap.getObjectMapper.registerModule(DefaultScalaModule)
  }

  override def run(environment: Environment) {
  }
}