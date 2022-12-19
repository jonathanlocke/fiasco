package digital.fiasco.libraries.logging;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface Logback
{
    Library logback_classic = library("ch.qos.logback:logback-classic");
    Library logback_core = library("ch.qos.logback:logback-core");
}
