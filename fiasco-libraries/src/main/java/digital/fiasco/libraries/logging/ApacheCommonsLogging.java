package digital.fiasco.libraries.logging;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheCommonsLogging
{
    Library apache_commons_logging = library("org.apache.commons:logging");
}
