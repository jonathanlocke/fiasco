package digital.fiasco.libraries.build;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Gradle
{
    Library gradle = library("com.android.tools.build:gradle");
    Library kotlin_gradle_plugin = library("org.jetbrains.kotlin:kotlin-gradle-plugin");
}
