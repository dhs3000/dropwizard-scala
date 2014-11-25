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

import javax.validation.Valid
import javax.validation.constraints.NotNull

import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty

import scala.beans.BeanProperty

trait DeploymentConfiguration extends Configuration {

  //def isProduction: Boolean = deploymentMode.toUpperCase == DeploymentConfiguration.production

  @BeanProperty
  @Valid
  @NotNull
  val deployment = new Deployent
}

class Deployent {
  private var _mode: DeploymentMode = Production

  def mode_=(mode: String): Unit = DeploymentMode(mode.toUpperCase) match {
    case Development => _mode = Development
    case _ => _mode = Production
  }

  def mode: DeploymentMode = _mode

  def isProduction: Boolean = mode == Production
}

sealed case class DeploymentMode(mode: String)

object Production extends DeploymentMode("PRODUCTION")

object Development extends DeploymentMode("DEVELOPMENT")

