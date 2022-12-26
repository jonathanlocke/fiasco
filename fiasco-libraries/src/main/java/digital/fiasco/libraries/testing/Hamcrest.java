package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.ArtifactGroup;
import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.ArtifactGroup.group;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Hamcrest extends LibraryGroups
{
    Library hamcrest         = hamcrest_group.library("hamcrest-all");
    Library hamcrest_library = hamcrest_group.library("hamcrest-library");
}
