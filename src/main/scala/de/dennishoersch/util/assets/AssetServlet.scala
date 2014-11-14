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

import java.net.URL
import javax.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

import com.google.common.base.CharMatcher
import com.google.common.io.Resources
import de.dennishoersch.util.logging.Logging
import de.dennishoersch.util.servlet._
import io.dropwizard.servlets.assets.ResourceURL

import scala.util.Try

class AssetServlet(
    _resourcePath: String,
    _uriPath: String,
    indexFile: Option[String],
    caching: Boolean)
  extends HttpServlet
  with Logging {


  private val slashes = CharMatcher.is('/')

  private val resourcePath = {
    val trimmedPath = trimSlashes(_resourcePath)
    if (trimmedPath.isEmpty) "" else s"$trimmedPath/"
  }

  private val uriPath = {
    val trimmedUri = trimSlashes(_uriPath)
    s"/$trimmedUri"
  }

  protected override def doGet(request: HttpServletRequest, response: HttpServletResponse) =
    Try {
      withAssetFor(request).respond(response)
    } recover {
      case e: Throwable =>
        error(s"Error serving request ${request.getRequestURI}?${request.getQueryString}", e)
        response.sendError(HttpServletResponse.SC_NOT_FOUND)
    }

  private def withAssetFor(request: HttpServletRequest): Asset = {
    val key = request.fullPath
    require(key.startsWith(uriPath))
    Asset(requestedAssetUrl(key), MediaType(request), caching)
  }

  private def requestedAssetUrl(key: String): Option[URL] = {
    val absolutePath = absoluteAssetPath(key)
    val result = Resources.getResource(absolutePath)
    if (ResourceURL.isDirectory(result))
      indexFile.map(file => Resources.getResource(s"$absolutePath/$file"))
    else
      Some(result)
  }

  private def absoluteAssetPath(key: String) = {
    val requestedResourcePath = trimSlashes(key.substring(uriPath.length))
    trimSlashes(resourcePath + requestedResourcePath)
  }

  private def trimSlashes(s: String) = slashes.trimFrom(s)
}
