package digital.fiasco.libraries.languages;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Clojure extends LibraryGroups
{
    Library clojure                 = clojure_group.library("clojure");
    Library clojure_contrib         = clojure_group.library("clojure-contrib");
    Library clojure_core_async      = clojure_group.library("core-async");
    Library clojure_core_cache      = clojure_group.library("core-cache");
    Library clojure_core_match      = clojure_group.library("core-match");
    Library clojure_data_codec      = clojure_group.library("data-codec");
    Library clojure_data_json       = clojure_group.library("data-json");
    Library clojure_java_classpath  = clojure_group.library("java-classpath");
    Library clojure_java_jdbc       = clojure_group.library("java-jdbc");
    Library clojure_script          = clojure_group.library("clojurescript");
    Library clojure_test_check      = clojure_group.library("test-check");
    Library clojure_tools_cli       = clojure_group.library("tools-cli");
    Library clojure_tools_logging   = clojure_group.library("tools-logging");
    Library clojure_tools_namespace = clojure_group.library("tools-namespace");
    Library clojure_tools_nrepl     = clojure_group.library("tools-nrepl");
    Library clojure_tools_reader    = clojure_group.library("tools-reader");
    Library clojure_tools_trace     = clojure_group.library("tools-trace");
    Library clojure_complete        = library("clojure-complete:clojure-complete");
}
