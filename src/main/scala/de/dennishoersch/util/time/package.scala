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
package de.dennishoersch.util

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormatterBuilder

package object time {

  private val formatter = new DateTimeFormatterBuilder()//.ofPattern("d.MM.uuuu HH:mm")
    .appendDayOfMonth(1)
    .appendLiteral('.')
    .appendMonthOfYear(2)
    .appendLiteral('.')
    .appendYear(4, 4)
    .appendLiteral(' ')
    .appendHourOfDay(2)
    .appendLiteral(':')
    .appendMinuteOfHour(2)
    .toFormatter

  implicit class AsDateTime(val str: String) {
    def asDateTime = LocalDateTime.parse(str, formatter)
  }

}