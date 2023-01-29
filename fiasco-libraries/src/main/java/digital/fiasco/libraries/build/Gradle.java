package digital.fiasco.libraries.build;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface Gradle extends LibraryGroups
{
    Library gradle = android_tools_build_group.library("gradle").asLibrary();
}
