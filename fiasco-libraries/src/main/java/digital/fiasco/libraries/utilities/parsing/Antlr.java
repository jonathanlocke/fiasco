package digital.fiasco.libraries.utilities.parsing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface Antlr extends LibraryGroups
{
    Library antlr               = antlr_group.library("antlr").asLibrary();
    Library antlr4              = antlr_group.library("antlr4").asLibrary();
    Library antlr4_annotations  = antlr_group.library("antlr4-annotations").asLibrary();
    Library antlr4_maven_plugin = antlr_group.library("antlr4-maven-plugin").asLibrary();
    Library antlr4_runtime      = antlr_group.library("antlr4-runtime").asLibrary();
    Library antlr_complete      = antlr_group.library("antlr-complete").asLibrary();
    Library antlr_runtime       = antlr_group.library("antlr-runtime").asLibrary();
}
