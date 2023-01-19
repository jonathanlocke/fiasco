package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused" })
public interface Lombok extends LibraryGroups
{
    Library lombok = lombok_group.library("lombok").asLibrary();
}
