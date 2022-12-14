package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface Parsing
{
    Library antlr = library("org.antlr:antlr");
    Library antlr4 = library("org.antlr:antlr4");
    Library antlr4_annotaions = library("org.antlr:antlr4-annotaions");
    Library antlr4_maven_plugin = library("org.antlr:antlr4-maven-plugin");
    Library antlr4_runtime = library("org.antlr:antlr4-runtime");
    Library antlr_complete = library("org.antlr:antlr-complete");
    Library antlr_runtime = library("org.antlr:antlr-runtime");
}
