package digital.fiasco.runtime.library.logging;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface CommonsLogging
{
    Library commons_logging = library("commons-logging:commons-logging");
}
