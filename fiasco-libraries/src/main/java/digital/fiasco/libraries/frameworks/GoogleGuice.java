package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface GoogleGuice extends LibraryGroups
{
    Library guice               = google_inject_group.library("guice");
    Library guice_multibindings = google_inject_extensions_group.library("guice-multibindings");
    Library guice_servlet       = google_inject_extensions_group.library("guice-servlet");
}
