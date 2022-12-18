package digital.fiasco.runtime.dependency.oss.languages;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Kotlin
{
    Library kotlin_stdlib = library("org.jetbrains.kotlin:kotlin-stdlib");
    Library kotlin_stdlib_common = library("org.jetbrains.kotlin:kotlin-stdlib-common");
}
