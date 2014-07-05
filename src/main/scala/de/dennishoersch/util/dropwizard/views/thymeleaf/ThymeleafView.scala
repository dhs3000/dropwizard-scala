package de.dennishoersch.util.dropwizard.views.thymeleaf

import io.dropwizard.views.View

class ThymeleafView(templateName: String) extends View("/" + templateName)