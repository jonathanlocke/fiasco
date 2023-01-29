package digital.fiasco.libraries.utilities.collections;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface ApacheCommonsCollections extends LibraryGroups
{
    Library apache_commons_collection4 = apache_commons_group.library("commons-collections4").asLibrary();
}
