package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface ApacheThrift extends LibraryGroups
{
    Library apache_thrift = apache_thrift_group.library("libthrift").asLibrary();
}
