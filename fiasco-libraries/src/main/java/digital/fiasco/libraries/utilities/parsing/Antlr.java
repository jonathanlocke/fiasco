package digital.fiasco.libraries.utilities.parsing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Antlr extends LibraryGroups
{
    Library antlr               = antlr_group.library("antlr");
    Library antlr4              = antlr_group.library("antlr4");
    Library antlr4_annotations  = antlr_group.library("antlr4-annotations");
    Library antlr4_maven_plugin = antlr_group.library("antlr4-maven-plugin");
    Library antlr4_runtime      = antlr_group.library("antlr4-runtime");
    Library antlr_complete      = antlr_group.library("antlr-complete");
    Library antlr_runtime       = antlr_group.library("antlr-runtime");
}
