package digital.fiasco.libraries.languages;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;

@SuppressWarnings("unused")
public interface Clojure extends LibraryGroups
{
    Library clojure                 = clojure_group.library("clojure").asLibrary();
    Library clojure_contrib         = clojure_group.library("clojure-contrib").asLibrary();
    Library clojure_core_async      = clojure_group.library("core-async").asLibrary();
    Library clojure_core_cache      = clojure_group.library("core-cache").asLibrary();
    Library clojure_core_match      = clojure_group.library("core-match").asLibrary();
    Library clojure_data_codec      = clojure_group.library("data-codec").asLibrary();
    Library clojure_data_json       = clojure_group.library("data-json").asLibrary();
    Library clojure_java_classpath  = clojure_group.library("java-classpath").asLibrary();
    Library clojure_java_jdbc       = clojure_group.library("java-jdbc").asLibrary();
    Library clojure_script          = clojure_group.library("clojurescript").asLibrary();
    Library clojure_test_check      = clojure_group.library("test-check").asLibrary();
    Library clojure_tools_cli       = clojure_group.library("tools-cli").asLibrary();
    Library clojure_tools_logging   = clojure_group.library("tools-logging").asLibrary();
    Library clojure_tools_namespace = clojure_group.library("tools-namespace").asLibrary();
    Library clojure_tools_nrepl     = clojure_group.library("tools-nrepl").asLibrary();
    Library clojure_tools_reader    = clojure_group.library("tools-reader").asLibrary();
    Library clojure_tools_trace     = clojure_group.library("tools-trace").asLibrary();
    Library clojure_complete        = library("clojure-complete:clojure-complete");
}
