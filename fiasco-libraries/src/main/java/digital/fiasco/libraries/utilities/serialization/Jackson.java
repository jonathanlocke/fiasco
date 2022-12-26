package digital.fiasco.libraries.utilities.serialization;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings("unused")
public interface Jackson extends LibraryGroups
{
    Library jackson_annotations = jackson_core_group.library("jackson-annotations");
    Library jackson_core        = jackson_core_group.library("jackson-core");
    Library jackson_databind    = jackson_core_group.library("jackson-databind");
}
