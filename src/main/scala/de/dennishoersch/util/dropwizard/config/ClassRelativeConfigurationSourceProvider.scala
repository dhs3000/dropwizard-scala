package de.dennishoersch.util.dropwizard.config

import io.dropwizard.configuration.ConfigurationSourceProvider
import java.io.InputStream

class ClassRelativeConfigurationSourceProvider(private val baseClass: Class[_]) extends ConfigurationSourceProvider {
  def this(obj: AnyRef) = this(obj.getClass)
  
  def open(path: String): InputStream = {
    baseClass.getResourceAsStream(path)
  }
}