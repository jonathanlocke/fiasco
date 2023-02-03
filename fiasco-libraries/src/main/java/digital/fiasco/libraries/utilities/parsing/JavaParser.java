package digital.fiasco.libraries.utilities.parsing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings({ "unused" })
public interface JavaParser extends LibraryGroups
{
    Library javaparser_core                     = javaparser_group.library("javaparser-core").asLibrary();
    Library javaparser_core_generators          = javaparser_group.library("javaparser-core-generators").asLibrary();
    Library javaparser_core_serialization       = javaparser_group.library("javaparser-core-serialization").asLibrary();
    Library javaparser_generator_utils          = javaparser_group.library("javaparser-generator-utils").asLibrary();
    Library javaparser_java_symbol_solver_core  = javaparser_group.library("java-symbol-solver-core").asLibrary();
    Library javaparser_java_symbol_solver_logic = javaparser_group.library("java-symbol-solver-logic").asLibrary();
    Library javaparser_java_symbol_solver_model = javaparser_group.library("java-symbol-solver-model").asLibrary();
    Library javaparser_metamodel                = javaparser_group.library("javaparser-metamodel").asLibrary();
    Library javaparser_metamodel_generator      = javaparser_group.library("javaparser-metamodel-generator").asLibrary();
    Library javaparser_symbol_solver_core       = javaparser_group.library("javaparser-symbol-solver-core").asLibrary();
    Library javaparser_symbol_solver_logic      = javaparser_group.library("javaparser-symbol-solver-logic").asLibrary();
    Library javaparser_symbol_solver_model      = javaparser_group.library("javaparser-symbol-solver-model").asLibrary();
}
