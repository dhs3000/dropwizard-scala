package de.dennishoersch.util.dropwizard.views.thymeleaf

import io.dropwizard.views.ViewRenderer
import io.dropwizard.views.View
import java.util.Locale
import java.io.OutputStream
import org.thymeleaf.templatemode.StandardTemplateModeHandlers
import org.thymeleaf.TemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import java.io.OutputStreamWriter
import org.thymeleaf.templateresolver.TemplateResolver
import org.thymeleaf.resourceresolver.IResourceResolver
import org.thymeleaf.TemplateProcessingParameters

private [thymeleaf] object DefaultThymeleafViewRenderer
  extends ThymeleafViewRenderer(StandardTemplateModeHandlers.HTML5.getTemplateModeName(), "/view/templates", ".html")

private [thymeleaf] class ThymeleafViewRenderer(mode: String, prefix: String, suffix: String) extends ViewRenderer {

  private val templateEngine = createTemplateEngine

  override def isRenderable(view: View) = view.isInstanceOf[ThymeleafView]

  override def render(view: View, locale: Locale, output: OutputStream) = {
    val writer = new OutputStreamWriter(output)
    templateEngine.process(view.getTemplateName(), new ViewContext(view), writer)
    writer.flush
  }

  private def createTemplateEngine(): TemplateEngine = {
    val engine = new TemplateEngine

    val resolver = new TemplateResolver
    resolver.setResourceResolver(new IResourceResolver() {
      def getName() = "ClassResourceLoader"
      def getResourceAsStream(templateProcessingParameters: TemplateProcessingParameters, templateName: String) = {
        this.getClass.getResourceAsStream(templateName)
      }
    })
    resolver.setTemplateMode(mode)
    resolver.setPrefix(prefix)
    resolver.setSuffix(suffix)
    // TODO: Cache time and how to resolve a template as cachable?
    //resolver.setCacheTTLMs(cacheTTLMs)

    engine.setTemplateResolver(resolver)
    engine.initialize()
    engine
  }
}