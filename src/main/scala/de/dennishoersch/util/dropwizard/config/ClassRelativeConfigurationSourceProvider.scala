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
package de.dennishoersch.util.dropwizard.config

import java.io.InputStream

import io.dropwizard.configuration.ConfigurationSourceProvider

class ClassRelativeConfigurationSourceProvider(private val baseClass: Class[_]) extends ConfigurationSourceProvider {
  def this(obj: AnyRef) = this(obj.getClass)
  
  def open(path: String): InputStream = {
    baseClass.getResourceAsStream(path)
  }
}