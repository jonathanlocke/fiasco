package digital.fiasco.runtime.dependency.oss.logging;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Log4j
{
    Library log4j = library("log4j:log4j");
}
