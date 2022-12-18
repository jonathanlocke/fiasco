package digital.fiasco.runtime.dependency.oss.logging;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface ApacheCommonsLogging
{
    Library apache_commons_logging = library("org.apache.commons:logging");
}
