package digital.fiasco.runtime.library.logging;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheCommonsLogging
{
    Library apache_commons_logging = library("org.apache.commons:logging");
}
