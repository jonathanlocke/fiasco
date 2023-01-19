package digital.fiasco.libraries.languages;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Kotlin extends LibraryGroups
{
    Library kotlin_stdlib        = kotlin_group.library("kotlin-stdlib").asLibrary();
    Library kotlin_stdlib_common = kotlin_group.library("kotlin-stdlib-common").asLibrary();
    Library kotlin_gradle_plugin = jetbrains_kotlin_group.library("kotlin-gradle-plugin").asLibrary();
}
