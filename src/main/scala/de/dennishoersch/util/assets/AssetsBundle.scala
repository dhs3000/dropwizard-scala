/*!
 * Copyright 2013-2014 Dennis Hörsch.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.dennishoersch.util.assets

import de.dennishoersch.util.dropwizard.config.DeploymentConfiguration
import io.dropwizard.{Configuration, ConfiguredBundle}
import io.dropwizard.setup.{Bootstrap, Environment}

class AssetsBundle private(
  name: String,
  resourcePath: String,
  uriPath: String,
  indexFile: Option[String],
  shouldAdd: DeploymentConfiguration => Boolean)
  extends ConfiguredBundle[DeploymentConfiguration] {

  override def initialize(bootstrap: Bootstrap[_]): Unit = {}

  override def run(configuration: DeploymentConfiguration, environment: Environment): Unit =
    if (shouldAdd(configuration))
      environment.servlets
        .addServlet(name, new AssetServlet(resourcePath, uriPath, indexFile, useCaching = configuration.deployment.isProduction))
        .addMapping(s"$uriPath*")
}

object AssetsBundle {

  def apply(name: String, resourcePath: String, uriPath: String): AssetsBundle =
    apply(name, resourcePath, uriPath, None)

  def apply(name: String, resourcePath: String, uriPath: String, indexFile: String): AssetsBundle =
    apply(name, resourcePath, uriPath, Some(indexFile))

  def apply(name: String, resourcePath: String, uriPath: String, indexFile: Option[String]): AssetsBundle =
    apply(name, resourcePath, uriPath, indexFile, (_) => true)

  def apply(
    name: String,
    resourcePath: String,
    uriPath: String,
    indexFile: Option[String],
    shouldAdd: DeploymentConfiguration => Boolean): AssetsBundle =

    new AssetsBundle(name, addSlash(resourcePath), addSlash(uriPath), indexFile, shouldAdd)

  private def addSlash(path: String) = if (path.endsWith("/")) path else path + '/'
}

object DevelopmentOnlyAssetsBundle {

  def apply(name: String, resourcePath: String, uriPath: String, indexFile: String): AssetsBundle =
    AssetsBundle(name, resourcePath, uriPath, Some(indexFile), _.deployment.isDevelopment)

}
