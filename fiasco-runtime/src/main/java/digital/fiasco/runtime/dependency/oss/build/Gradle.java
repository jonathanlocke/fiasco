package digital.fiasco.runtime.dependency.oss.build;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Gradle
{
    Library gradle = library("com.android.tools.build:gradle");
    Library kotlin_gradle_plugin = library("org.jetbrains.kotlin:kotlin-gradle-plugin");
}
