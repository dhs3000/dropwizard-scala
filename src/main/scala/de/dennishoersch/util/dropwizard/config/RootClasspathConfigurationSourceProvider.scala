package de.dennishoersch.util.dropwizard.config

import java.io.InputStream

import io.dropwizard.configuration.ConfigurationSourceProvider

object RootClasspathConfigurationSourceProvider extends RootClasspathConfigurationSourceProvider

sealed class RootClasspathConfigurationSourceProvider extends ConfigurationSourceProvider {
  
  def open(path: String): InputStream = {
    this.getClass.getResourceAsStream(s"/$path")
  }
}