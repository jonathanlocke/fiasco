package digital.fiasco.runtime.library.languages;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Kotlin
{
    Library kotlin_stdlib = library("org.jetbrains.kotlin:kotlin-stdlib");
    Library kotlin_stdlib_common = library("org.jetbrains.kotlin:kotlin-stdlib-common");
}
