package de.dennishoersch.util.dropwizard.views.thymeleaf

import java.io.OutputStream
import java.io.OutputStreamWriter
import java.util.Locale

import scala.collection.JavaConversions._

import org.thymeleaf.TemplateEngine
import org.thymeleaf.TemplateProcessingParameters
import org.thymeleaf.resourceresolver.IResourceResolver
import org.thymeleaf.templatemode.StandardTemplateModeHandlers
import org.thymeleaf.templateresolver.TemplateResolver

import io.dropwizard.views.View
import io.dropwizard.views.ViewRenderer

private[thymeleaf] object DefaultThymeleafViewRenderer
  extends ThymeleafViewRenderer(
    mode = StandardTemplateModeHandlers.HTML5.getTemplateModeName(),
    prefix = "/view/templates",
    suffix = ".html",
    cacheTTLMs = Some(1000 * 60 * 60)
  )

private[thymeleaf] class ThymeleafViewRenderer(
  mode: String,
  prefix: String,
  suffix: String,
  cacheTTLMs: Option[Long] = None)
  extends ViewRenderer {

  private val templateEngine = {
    val engine = new TemplateEngine

    val resolver = new TemplateResolver
    resolver.setResourceResolver(DirectClassesourceResolver)
    resolver.setTemplateMode(mode)
    resolver.setPrefix(prefix)
    resolver.setSuffix(suffix)
    if (cacheTTLMs.isDefined) {
      resolver.setCacheTTLMs(cacheTTLMs.get)
    }

    engine.setTemplateResolver(resolver)
    engine.initialize()

    engine
  }

  override def isRenderable(view: View) = view.isInstanceOf[ThymeleafView]

  override def render(view: View, locale: Locale, output: OutputStream) = {
    val writer = new OutputStreamWriter(output)
    templateEngine.process(view.getTemplateName(), new ViewContext(view), writer)
    writer.flush
  }

  object DirectClassesourceResolver extends IResourceResolver {
    def getName() = getClass.getSimpleName
    def getResourceAsStream(templateProcessingParameters: TemplateProcessingParameters, templateName: String) = {
      this.getClass.getResourceAsStream(templateName)
    }
  }

}

