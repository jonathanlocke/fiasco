package digital.fiasco.libraries.utilities.language;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface ApacheCommonsLang extends LibraryGroups
{
    Library apache_commons_lang3 = apache_commons_group.library("commons-lang3").asLibrary();
}
