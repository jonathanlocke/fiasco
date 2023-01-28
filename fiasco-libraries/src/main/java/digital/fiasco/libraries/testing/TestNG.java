package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface TestNG extends LibraryGroups
{
    Library test_ng = test_ng_group.library("testng").asLibrary();
}
