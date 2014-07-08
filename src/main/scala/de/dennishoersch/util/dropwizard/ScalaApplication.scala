package de.dennishoersch.util.dropwizard

import io.dropwizard.Application

import io.dropwizard.Configuration

abstract class ScalaApplication[T <: Configuration] extends Application[T] {
  final def main(args: Array[String]) = run(args)
}