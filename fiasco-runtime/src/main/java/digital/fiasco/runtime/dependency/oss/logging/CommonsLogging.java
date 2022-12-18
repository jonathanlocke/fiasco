package digital.fiasco.runtime.dependency.oss.logging;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface CommonsLogging
{
    Library commons_logging = library("commons-logging:commons-logging");
}
