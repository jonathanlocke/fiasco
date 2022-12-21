package digital.fiasco.libraries.languages;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Clojure
{
    Library clojure = library("org.clojure:clojure");
    Library clojure_complete = library("clojure-complete_clojure-complete");
}
