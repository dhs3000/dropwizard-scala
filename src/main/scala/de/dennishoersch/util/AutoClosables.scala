package de.dennishoersch.util

object AutoClosables {

  def tryWith[A <: AutoCloseable, B](closable: A)(block: A => B) =
    try {
      block(closable)
    } finally {
      closable.close()
    }
}
