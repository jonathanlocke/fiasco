package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface AssertJ extends LibraryGroups
{
    Library assertj_core  = assertj_group.library("assertj-core").asLibrary();
    Library assertj_guava = assertj_group.library("assertj-guava").asLibrary();
}
