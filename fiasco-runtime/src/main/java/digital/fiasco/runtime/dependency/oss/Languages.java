package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Languages
{
    Library clojure = library("org.clojure:clojure");
    Library clojure_complete = library("clojure-complete_clojure-complete");
    Library groovy_all = library("org.codehaus.groovy:groovy-all");
    Library kotlin_stdlib = library("org.jetbrains.kotlin:kotlin-stdlib");
    Library kotlin_stdlib_common = library("org.jetbrains.kotlin:kotlin-stdlib-common");
    Library scala_library = library("org.scala-lang:scala-library");
    Library scala_reflect = library("org.scala-lang:scala-reflect");
    Library scala_test = library("org.scalatest:scalatest");
}
