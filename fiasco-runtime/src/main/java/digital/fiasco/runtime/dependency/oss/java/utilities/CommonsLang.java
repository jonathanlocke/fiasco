package digital.fiasco.runtime.dependency.oss.java.utilities;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface CommonsLang
{
    Library commons_lang = library("commons-lang:commons-lang");
    Library commons_lang3 = library("org.apache.commons:commons-lang3");
}
