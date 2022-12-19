package digital.fiasco.runtime.library.languages;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Scala
{
    Library scala_library = library("org.scala-lang:scala-library");
    Library scala_reflect = library("org.scala-lang:scala-reflect");
    Library scala_test = library("org.scalatest:scalatest");
}
