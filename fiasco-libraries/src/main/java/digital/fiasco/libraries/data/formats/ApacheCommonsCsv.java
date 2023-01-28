package digital.fiasco.libraries.data.formats;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface ApacheCommonsCsv extends LibraryGroups
{
    Library apache_commons_csv = apache_commons_group.library("commons-csv").asLibrary();
}
