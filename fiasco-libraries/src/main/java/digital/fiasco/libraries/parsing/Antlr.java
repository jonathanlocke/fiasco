package digital.fiasco.libraries.parsing;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Antlr
{
    Library antlr = library("org.antlr:antlr");
    Library antlr4 = library("org.antlr:antlr4");
    Library antlr4_annotations = library("org.antlr:antlr4-annotations");
    Library antlr4_maven_plugin = library("org.antlr:antlr4-maven-plugin");
    Library antlr4_runtime = library("org.antlr:antlr4-runtime");
    Library antlr_complete = library("org.antlr:antlr-complete");
    Library antlr_runtime = library("org.antlr:antlr-runtime");
}