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
package de.dennishoersch.util.json

import java.io.ByteArrayOutputStream

import scala.util.Try

import org.apache.commons.lang.StringEscapeUtils

import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Json extends Json 

class Json {
  private val mapper = createObjectMapper
  
  def stringify(obj: Any): Try[String] = Try({

    val result = new ByteArrayOutputStream
    val jsonGenerator = mapper.getFactory.createGenerator(result, JsonEncoding.UTF8)

    jsonGenerator.useDefaultPrettyPrinter

    mapper.writeValue(jsonGenerator, obj)

    result.toString
  })

  private def createObjectMapper: ObjectMapper = {
    val result = new ObjectMapper
    val module = new SimpleModule
    module.addDeserializer(classOf[String], HtmlEscapingStringDeserializer)
    result.registerModule(module)
    result.registerModule(DefaultScalaModule)
    result
  }

  private object HtmlEscapingStringDeserializer extends JsonDeserializer[String] {
    private val delegate = StringDeserializer.instance

    override def deserialize(jp: JsonParser, ctxt: DeserializationContext) 
    	= StringEscapeUtils.escapeHtml(delegate.deserialize(jp, ctxt))

    override def deserializeWithType(jp: JsonParser, ctxt: DeserializationContext, typeDeserializer: TypeDeserializer)
    	= StringEscapeUtils.escapeHtml(delegate.deserializeWithType(jp, ctxt, typeDeserializer))
  }
}