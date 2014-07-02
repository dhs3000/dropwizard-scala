package de.dennishoersch.dropwizard.util.json

import java.io.ByteArrayOutputStream
import java.io.IOException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.module.SimpleModule
import scala.util.Try
import com.fasterxml.jackson.core.JsonEncoding
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.deser.std.StringDeserializer
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.core.JsonParser
import org.apache.commons.lang.StringEscapeUtils
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer
import com.fasterxml.jackson.module.scala.DefaultScalaModule

object Json extends Json 

class Json {
  private val mapper = createObjectMapper
  
  def stringify(obj: Any): Try[String] = Try({

    val result = new ByteArrayOutputStream
    val jsonGenerator = mapper.getFactory().createGenerator(result, JsonEncoding.UTF8)

    jsonGenerator.useDefaultPrettyPrinter

    mapper.writeValue(jsonGenerator, obj)

    result.toString
  })

  private def createObjectMapper(): ObjectMapper = {
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