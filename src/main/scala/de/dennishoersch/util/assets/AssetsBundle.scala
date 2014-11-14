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
