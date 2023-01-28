package digital.fiasco.libraries.utilities.text;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings("unused")
public interface ApacheCommonsText extends LibraryGroups
{
    Library apache_commons_text = apache_commons_group.library("commons-text").asLibrary();
}
