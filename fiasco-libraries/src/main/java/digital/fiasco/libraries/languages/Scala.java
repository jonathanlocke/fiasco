package digital.fiasco.libraries.languages;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Scala
{
    Library scala_library = library("org.scala-lang:scala-library");
    Library scala_reflect = library("org.scala-lang:scala-reflect");
    Library scala_test = library("org.scalatest:scalatest");
}
