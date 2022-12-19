package digital.fiasco.runtime.library.logging;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Slf4j
{
    Library slf4j_api = library("org.slf4j:slf4j-api");
    Library slf4j_log4j12 = library("org.slf4j:slf4j-log4j12");
    Library slf4j_simple = library("org.slf4j:slf4j-simple");
    Library jcl_over_slf4j = library("org.slf4j:jcl-over-slf4j");
}
