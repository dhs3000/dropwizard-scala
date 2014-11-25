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
import javax.servlet.http.HttpServletResponse

import com.google.common.io.Resources
import com.google.common.net.HttpHeaders
import de.dennishoersch.util.AutoClosables._
import de.dennishoersch.util.logging.Logging

private[assets] object Asset {
  def apply(assetPath: Option[URL], mediaType: MediaTypeSetter, useCaching: Boolean): Asset =
    assetPath
      .map(asAsset(_, mediaType, useCaching))
      .getOrElse(NotFoundAsset)

  private def asAsset(assetPath: URL, mediaType: MediaTypeSetter, useCaching: Boolean) =
    new DefaultAsset(assetPath, mediaType, modifyResponseFor(useCaching))

  private def modifyResponseFor(useCaching: Boolean) = if (useCaching) Caching else NotCaching
}

trait Asset {
  def respond(response: HttpServletResponse)
}

private[assets] object NotFoundAsset extends Asset {
  override def respond(response: HttpServletResponse): Unit =
    response.sendError(HttpServletResponse.SC_NOT_FOUND)
}

trait ResponseModifier {
  def modify(currentDateMilliseconds: Long, response: HttpServletResponse)
}

private[assets] class DefaultAsset(
  assetPath: URL,
  responseModifier: ResponseModifier*)
  extends Asset
  with Logging {

  private final val asset = Resources.toByteArray(assetPath)

  final def respond(response: HttpServletResponse): Unit = {
    val currentDateMilliseconds = System.currentTimeMillis
    setDefaultHeaders(currentDateMilliseconds, response)
    responseModifier.foreach(_.modify(currentDateMilliseconds, response))
    writeData(response)
  }

  private def setDefaultHeaders(currentDateMilliseconds: Long, response: HttpServletResponse): Unit = {
    response.setDateHeader(HttpHeaders.DATE, currentDateMilliseconds)
  }

  private def writeData(response: HttpServletResponse): Unit =
    tryWith(response.getOutputStream) { out =>
      out.write(asset)
    }
}

private object NotCaching extends ResponseModifier {
  override def modify(currentDateMilliseconds: Long, response: HttpServletResponse): Unit = {
    response.setDateHeader(HttpHeaders.EXPIRES, 0)
    response.setHeader(HttpHeaders.PRAGMA, "no-cache")
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache")
    response.addHeader(HttpHeaders.CACHE_CONTROL, "no-store")
    response.addHeader(HttpHeaders.CACHE_CONTROL, "must-revalidate")
  }
}

private object Caching extends ResponseModifier {
  private val maxAgeSeconds: Long = 364 * 24 * 60 * 60
  private val maxAgeMilliseconds: Long = maxAgeSeconds * 1000
  private val maxAgeHeader = s"max-age=$maxAgeSeconds"

  override def modify(currentDateMilliseconds: Long, response: HttpServletResponse): Unit = {
    response.setDateHeader(HttpHeaders.EXPIRES, currentDateMilliseconds + maxAgeMilliseconds)
    response.setHeader(HttpHeaders.PRAGMA, "cache")
    response.setHeader(HttpHeaders.CACHE_CONTROL, "public")
    response.addHeader(HttpHeaders.CACHE_CONTROL, maxAgeHeader)
  }
}



