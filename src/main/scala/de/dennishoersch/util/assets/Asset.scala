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
import de.dennishoersch.util.guava._
import de.dennishoersch.util.logging.Logging

trait Asset {
  def respond(implicit response: HttpServletResponse): Unit
}

private[assets] object Asset {
  def apply(assetPath: Option[URL], mediaType: MediaType, caching: Boolean): Asset = {

    def asAsset(assetPath: URL) =
      if (caching) new CachedAsset(assetPath, mediaType)
      else new NotCachedAsset(assetPath, mediaType)

    assetPath.map(asAsset).getOrElse(NotFoundAsset)
  }
}

private[assets] object NotFoundAsset extends Asset {
  override def respond(implicit response: HttpServletResponse): Unit =
    response.sendError(HttpServletResponse.SC_NOT_FOUND)
}

private[assets] sealed abstract class AbstractAsset(
    assetPath: URL,
    mediaType: MediaType)
  extends Asset
  with Logging {

  private final val asset = Resources.toByteArray(assetPath)

  override def respond(implicit response: HttpServletResponse): Unit = {
    setHeaders()
    writeData()
  }

  protected final val currentDateMilliseconds = System.currentTimeMillis

  private def setHeaders()(implicit response: HttpServletResponse): Unit = {
    val md = mediaType.get
    response.setContentType(s"${md.`type`}/${md.subtype}")
    md.charset.foreach(c => response.setCharacterEncoding(c.toString))

    response.setDateHeader(HttpHeaders.DATE, currentDateMilliseconds)
    setCacheHeaders()
  }

  protected def setCacheHeaders()(implicit response: HttpServletResponse): Unit

  private def writeData()(implicit response: HttpServletResponse): Unit =
    tryWith(response.getOutputStream) { out =>
      out.write(asset)
    }
}

private[assets] class NotCachedAsset(
    assetPath: URL,
    mediaType: MediaType)
  extends AbstractAsset(
    assetPath,
    mediaType) {

  override def setCacheHeaders()(implicit response: HttpServletResponse): Unit = {
    response.setDateHeader(HttpHeaders.EXPIRES, 0)
    response.setHeader(HttpHeaders.PRAGMA, "no-cache")
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache")
    response.addHeader(HttpHeaders.CACHE_CONTROL, "no-store")
    response.addHeader(HttpHeaders.CACHE_CONTROL, "must-revalidate")
  }
}

private[assets] class CachedAsset(
    assetPath: URL,
    mediaType: MediaType)
  extends AbstractAsset(
    assetPath,
    mediaType) {

  import de.dennishoersch.util.assets.CachedAsset._

  override def setCacheHeaders()(implicit response: HttpServletResponse): Unit = {
    response.setDateHeader(HttpHeaders.EXPIRES, currentDateMilliseconds + maxAgeMilliseconds)
    response.setHeader(HttpHeaders.PRAGMA, "cache")
    response.setHeader(HttpHeaders.CACHE_CONTROL, "public")
    response.addHeader(HttpHeaders.CACHE_CONTROL, maxAgeHeader)
  }
}

private object CachedAsset {
  final val maxAgeSeconds: Long = 364 * 24 * 60 * 60
  final val maxAgeMilliseconds: Long = maxAgeSeconds * 1000
  final val maxAgeHeader = s"max-age=$maxAgeSeconds"
}


