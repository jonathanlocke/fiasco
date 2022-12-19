package digital.fiasco.runtime.library.languages.java.utilities;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface CommonsLang
{
    Library commons_lang = library("commons-lang:commons-lang");
    Library commons_lang3 = library("org.apache.commons:commons-lang3");
}
