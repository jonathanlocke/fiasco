package digital.fiasco.libraries.languages;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Kotlin
{
    Library kotlin_stdlib = library("org.jetbrains.kotlin:kotlin-stdlib");
    Library kotlin_stdlib_common = library("org.jetbrains.kotlin:kotlin-stdlib-common");
}
