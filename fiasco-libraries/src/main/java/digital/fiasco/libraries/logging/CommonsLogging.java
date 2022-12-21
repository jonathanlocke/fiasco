package digital.fiasco.libraries.logging;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface CommonsLogging
{
    Library commons_logging = library("commons-logging:commons-logging");
}
