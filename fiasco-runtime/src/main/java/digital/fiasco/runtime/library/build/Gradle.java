package digital.fiasco.runtime.library.build;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Gradle
{
    Library gradle = library("com.android.tools.build:gradle");
    Library kotlin_gradle_plugin = library("org.jetbrains.kotlin:kotlin-gradle-plugin");
}
