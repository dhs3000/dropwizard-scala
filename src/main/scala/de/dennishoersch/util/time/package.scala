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
    .toFormatter()

  implicit class AsDateTime(val str: String) {
    def asDateTime = LocalDateTime.parse(str, formatter)
  }

}