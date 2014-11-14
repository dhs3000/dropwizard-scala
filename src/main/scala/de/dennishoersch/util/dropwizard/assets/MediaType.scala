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
