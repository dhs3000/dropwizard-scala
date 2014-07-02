package de.dennishoersch.dropwizard.util.dropwizard

import io.dropwizard.configuration.ConfigurationSourceProvider
import java.io.InputStream

object RootClasspathConfigurationSourceProvider extends RootClasspathConfigurationSourceProvider

sealed class RootClasspathConfigurationSourceProvider extends ConfigurationSourceProvider {
  
  def open(path: String): InputStream = {
    this.getClass.getResourceAsStream(s"/$path")
  }
}