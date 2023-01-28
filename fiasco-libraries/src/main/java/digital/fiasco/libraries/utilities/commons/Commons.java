package digital.fiasco.libraries.utilities.commons;

import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

import static digital.fiasco.runtime.dependency.artifact.artifacts.Library.library;

@SuppressWarnings("unused")
public interface Commons
{
    Library commons_codec       = library("commons-codec:commons-codec");
    Library commons_beanutils   = library("commons-beanutils:commons-beanutils");
    Library commons_logging     = library("commons-logging:commons-logging");
    Library commons_lang        = library("commons-lang:commons-lang");
    Library commons_io          = library("commons-io:commons-io");
    Library commons_collections = library("commons-collections:commons-collections");
}
