package digital.fiasco.libraries.logging;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Log4j
{
    Library log4j = library("log4j:log4j");
}
