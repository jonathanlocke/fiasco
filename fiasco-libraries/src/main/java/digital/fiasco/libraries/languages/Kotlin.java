package digital.fiasco.libraries.languages;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Kotlin extends LibraryGroups
{
    Library kotlin_stdlib        = kotlin_group.library("kotlin-stdlib");
    Library kotlin_stdlib_common = kotlin_group.library("kotlin-stdlib-common");
    Library kotlin_gradle_plugin = jetbrains_kotlin_group.library("kotlin-gradle-plugin");
}
