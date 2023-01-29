package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface GoogleGuava extends LibraryGroups
{
    Library guava                   = google_guava_group.library("guava").asLibrary();
    Library guava_collections       = google_guava_group.library("guava-collections").asLibrary();
    Library guava_listenable_future = google_guava_group.library("guava-listenable-future").asLibrary();
    Library guava_gwt               = google_guava_group.library("guava-gwt").asLibrary();
    Library guava_retrying          = google_guava_group.library("guava-retrying").asLibrary();
    Library guava_test              = google_guava_group.library("guava-testlib").asLibrary();
}
