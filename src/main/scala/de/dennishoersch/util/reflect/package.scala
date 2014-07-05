package de.dennishoersch.util

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

package object reflect {
  private val mirror = scala.reflect.runtime.currentMirror

  def accessorsOf[T](obj: T): Map[String, MethodSymbol] = mirror.classSymbol(obj.getClass).toType.members.collect {
    case m: MethodSymbol if m.isGetter && m.isPublic => m
  }.map(accessor => (accessor.name.decodedName.toString, accessor)).toMap

  def propertiesWithValuesOf[T](obj: T): Map[String, Any] = {

    val instanceMirror = mirror.reflect(obj)(ClassTag(obj.getClass))
    accessorsOf(obj).map { accessor => (accessor._1, instanceMirror.reflectMethod(accessor._2).apply()) }
  }

  def propertyValue[T](obj: T, propertyName: String): Option[Any] = {
    accessorsOf(obj).get(propertyName).map(accessor => {
       val instanceMirror = mirror.reflect(obj)(ClassTag(obj.getClass))
       instanceMirror.reflectMethod(accessor).apply()
    })
  }
}