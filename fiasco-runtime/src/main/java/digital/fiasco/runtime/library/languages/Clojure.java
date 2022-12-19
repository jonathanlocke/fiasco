package digital.fiasco.runtime.library.languages;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Clojure
{
    Library clojure = library("org.clojure:clojure");
    Library clojure_complete = library("clojure-complete_clojure-complete");
}
