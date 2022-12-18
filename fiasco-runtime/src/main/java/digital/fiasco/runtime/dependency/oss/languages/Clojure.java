package digital.fiasco.runtime.dependency.oss.languages;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Clojure
{
    Library clojure = library("org.clojure:clojure");
    Library clojure_complete = library("clojure-complete_clojure-complete");
}
