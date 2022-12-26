package digital.fiasco.libraries.frameworks;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface GoogleGuava extends LibraryGroups
{
    Library guava                   = google_guava_group.library("guava");
    Library guava_collections       = google_guava_group.library("guava-collections");
    Library guava_listenable_future = google_guava_group.library("guava-listenable-future");
    Library guava_gwt               = google_guava_group.library("guava-gwt");
    Library guava_retrying          = google_guava_group.library("guava-retrying");
    Library guava_test              = google_guava_group.library("guava-testlib");
}
