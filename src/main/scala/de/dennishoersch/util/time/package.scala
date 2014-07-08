package de.dennishoersch.util

import java.time.LocalDateTime

import java.time.format.DateTimeFormatter

package object time {
  
  private val formatter = DateTimeFormatter.ofPattern("d.MM.uuuu HH:mm")

  implicit class AsDateTime(val str: String) {
    def asDateTime = LocalDateTime.parse(str, formatter)
  }
}