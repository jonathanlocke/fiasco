package digital.fiasco.libraries.languages;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.artifacts.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Scala extends LibraryGroups
{
    Library scala_library = scala_lang_group.library("scala-library").asLibrary();
    Library scala_reflect = scala_lang_group.library("scala-reflect").asLibrary();
    Library scala_test    = scala_test_group.library("scalatest").asLibrary();
}
