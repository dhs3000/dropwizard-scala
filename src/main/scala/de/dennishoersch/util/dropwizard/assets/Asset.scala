
package de.dennishoersch.util.dropwizard.assets

import java.net.URL
import java.nio.charset.Charset
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import com.google.common.base.Charsets
import com.google.common.hash.Hashing
import com.google.common.io.Resources
import com.google.common.net.{HttpHeaders, MediaType}
import de.dennishoersch.util.AutoClosables._
import de.dennishoersch.util.guava._
import de.dennishoersch.util.logging.Logging
import io.dropwizard.servlets.assets.ResourceURL

import scala.util.{Failure, Success, Try}

trait Asset {
  def respond(implicit response: HttpServletResponse): Unit
}

object Asset {
  def apply(assetUrl: Option[URL], mediaType: MediaType, caching: Boolean): Asset = {

    def toAsset(assetUrl: URL) =
      if (caching) new CachedAsset(assetUrl, mediaType)
      else new NotCachedAsset(assetUrl, mediaType)

    assetUrl.map(toAsset).getOrElse(NotFoundAsset)
  }
}

object NotFoundAsset extends Asset {
  override def respond(implicit response: HttpServletResponse): Unit = response.sendError(HttpServletResponse.SC_NOT_FOUND)
}

sealed abstract class AbstractAsset(
    resourceUrl: URL,
    mediaType: MediaType)
  extends Asset
  with Logging {

  private final val resource = Resources.toByteArray(resourceUrl)

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
      out.write(resource)
    }
}

final class NotCachedAsset(
    resourceUrl: URL,
    mediaType: MediaType)
  extends AbstractAsset(
    resourceUrl,
    mediaType) {

  override def setCacheHeaders()(implicit response: HttpServletResponse): Unit = {
    response.setDateHeader(HttpHeaders.EXPIRES, 0)
    response.setHeader(HttpHeaders.PRAGMA, "no-cache")
    response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache")
    response.addHeader(HttpHeaders.CACHE_CONTROL, "no-store")
    response.addHeader(HttpHeaders.CACHE_CONTROL, "must-revalidate")
  }
}

final class CachedAsset(
    resourceUrl: URL,
    mediaType: MediaType)
  extends AbstractAsset(
    resourceUrl,
    mediaType) {

  import CachedAsset._

  override def setCacheHeaders()(implicit response: HttpServletResponse): Unit = {
    response.setDateHeader(HttpHeaders.EXPIRES, currentDateMilliseconds + maxAgeMilliseconds)
    response.setHeader(HttpHeaders.PRAGMA, "cache")
    response.setHeader(HttpHeaders.CACHE_CONTROL, "public")
    response.addHeader(HttpHeaders.CACHE_CONTROL, maxAgeHeader)
  }
}

object CachedAsset {
  final val maxAgeSeconds: Long = 364 * 24 * 60 * 60
  final val maxAgeMilliseconds: Long = maxAgeSeconds * 1000
  final val maxAgeHeader = s"max-age=$maxAgeSeconds"
}


