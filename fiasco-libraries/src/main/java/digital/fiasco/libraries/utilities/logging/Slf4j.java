package digital.fiasco.libraries.utilities.logging;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface Slf4j extends LibraryGroups
{
    Library slf4j_api      = slf4j_group.library("slf4j-api").asLibrary();
    Library slf4j_log4j12  = slf4j_group.library("slf4j-log4j12").asLibrary();
    Library slf4j_simple   = slf4j_group.library("slf4j-simple").asLibrary();
    Library jcl_over_slf4j = slf4j_group.library("jcl-over-slf4j").asLibrary();
}
