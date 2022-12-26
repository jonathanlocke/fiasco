package digital.fiasco.libraries.languages;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.Library;

@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public interface Scala extends LibraryGroups
{
    Library scala_library = scala_lang_group.library("scala-library");
    Library scala_reflect = scala_lang_group.library("scala-reflect");
    Library scala_test    = scala_test_group.library("scalatest");
}
