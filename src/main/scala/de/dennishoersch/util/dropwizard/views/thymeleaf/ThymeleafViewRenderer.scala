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
import nz.net.ultraq.thymeleaf.LayoutDialect
import org.thymeleaf.messageresolver.StandardMessageResolver

private[thymeleaf] object DefaultThymeleafViewRenderer
  extends ThymeleafViewRenderer(
    mode = StandardTemplateModeHandlers.HTML5.getTemplateModeName(),
    prefix = "/view/templates",
    suffix = ".html",
    cacheTTLMs = Some(1000 * 60 * 60))

private[thymeleaf] class ThymeleafViewRenderer(
  mode: String,
  prefix: String,
  suffix: String,
  cacheTTLMs: Option[Long] = None)
  extends ViewRenderer {

  private val templateEngine = {
    val engine = new TemplateEngine

    engine.setTemplateResolver {
      val templateResolver = new TemplateResolver

      templateResolver.setResourceResolver(DirectClassesourceResolver)
      templateResolver.setTemplateMode(mode)
      templateResolver.setPrefix(prefix)
      templateResolver.setSuffix(suffix)
      if (cacheTTLMs.isDefined) {
        templateResolver.setCacheTTLMs(cacheTTLMs.get)
      }

      templateResolver
    }

    engine.addDialect(new LayoutDialect)

    engine.initialize

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

