package de.dennishoersch.util.dropwizard.config

import java.io.InputStream

import io.dropwizard.configuration.ConfigurationSourceProvider

class ClassRelativeConfigurationSourceProvider(private val baseClass: Class[_]) extends ConfigurationSourceProvider {
  def this(obj: AnyRef) = this(obj.getClass)
  
  def open(path: String): InputStream = {
    baseClass.getResourceAsStream(path)
  }
}