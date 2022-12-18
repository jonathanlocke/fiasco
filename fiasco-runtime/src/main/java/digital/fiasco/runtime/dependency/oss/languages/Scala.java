package digital.fiasco.runtime.dependency.oss.languages;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Scala
{
    Library scala_library = library("org.scala-lang:scala-library");
    Library scala_reflect = library("org.scala-lang:scala-reflect");
    Library scala_test = library("org.scalatest:scalatest");
}
