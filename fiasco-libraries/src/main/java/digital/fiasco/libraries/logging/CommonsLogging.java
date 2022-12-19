package digital.fiasco.libraries.logging;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface CommonsLogging
{
    Library commons_logging = library("commons-logging:commons-logging");
}
