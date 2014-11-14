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