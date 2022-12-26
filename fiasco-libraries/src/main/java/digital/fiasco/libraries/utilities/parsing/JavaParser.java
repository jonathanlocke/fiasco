package digital.fiasco.libraries.utilities.parsing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public interface JavaParser extends LibraryGroups
{
    Library javaparser_core                     = javaparser_group.library("javaparser-core");
    Library javaparser_core_generators          = javaparser_group.library("javaparser-core-generators");
    Library javaparser_core_serialization       = javaparser_group.library("javaparser-core-serialization");
    Library javaparser_generator_utils          = javaparser_group.library("javaparser-generator-utils");
    Library javaparser_java_symbol_solver_core  = javaparser_group.library("java-symbol-solver-core");
    Library javaparser_java_symbol_solver_logic = javaparser_group.library("java-symbol-solver-logic");
    Library javaparser_java_symbol_solver_model = javaparser_group.library("javs-symbol-solver-model");
    Library javaparser_metamodel                = javaparser_group.library("javaparser-metamodel");
    Library javaparser_metamodel_generator      = javaparser_group.library("javaparser-metamodel-generator");
    Library javaparser_symbol_solver_core       = javaparser_group.library("javaparser-symbol-solver-core");
    Library javaparser_symbol_solver_logic      = javaparser_group.library("javaparser-symbol-solver-logic");
    Library javaparser_symbol_solver_model      = javaparser_group.library("javaparser-symbol-solver-model");
}
