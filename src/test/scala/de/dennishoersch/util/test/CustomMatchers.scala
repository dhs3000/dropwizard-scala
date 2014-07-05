package de.dennishoersch.util.test

import org.scalatest.matchers.BePropertyMatchResult
import org.scalatest.matchers.BePropertyMatcher

import scala.reflect.runtime.universe._

trait CustomMatchers {

  def anInstanceOf[T](implicit manifest: Manifest[T]) = {
    val clazz = manifest.erasure.asInstanceOf[Class[T]]
    new BePropertyMatcher[AnyRef] {
      def apply(left: AnyRef) =
        BePropertyMatchResult(left.getClass.isAssignableFrom(clazz), "aninstance of " + clazz.getName)
    }
  }
}