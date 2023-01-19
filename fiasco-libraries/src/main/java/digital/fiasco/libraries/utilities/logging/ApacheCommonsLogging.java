package digital.fiasco.libraries.utilities.logging;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface ApacheCommonsLogging extends LibraryGroups
{
    Library apache_commons_logging = apache_commons_group.library("logging").asLibrary();
}
