package de.dennishoersch.util.dropwizard.assets

import javax.servlet.http.HttpServletRequest

import com.google.common.base.Charsets
import com.google.common.net.{MediaType => GuavaMediaType}
import de.dennishoersch.util.logging.Logging

import scala.util.{Failure, Success, Try}

class MediaType(request: HttpServletRequest) extends Logging {

  private final val defaultMediaType = GuavaMediaType.HTML_UTF_8
  private final val defaultCharset = Charsets.UTF_8

  def get: GuavaMediaType = {
    def fromMimeType(mimeType: String) =
      Try {
        val mediaType = GuavaMediaType.parse(mimeType)
        if (mediaType.is(GuavaMediaType.ANY_TEXT_TYPE))
          mediaType.withCharset(defaultCharset)
        else
          mediaType
      } match {
        case Success(mediaType) =>
          Some(mediaType)
        case Failure(e) =>
          error(s"Error getting mediaType of $mimeType!", e)
          None
      }

    val mimeTypeOfExtension = Option(request.getServletContext.getMimeType(request.getRequestURI))
    mimeTypeOfExtension.flatMap(fromMimeType).getOrElse(defaultMediaType)
  }
}

object MediaType {
  def apply(request: HttpServletRequest) = new MediaType(request)
}
