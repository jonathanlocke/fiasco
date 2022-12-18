package digital.fiasco.runtime.dependency.oss.logging;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Logback
{
    Library logback_classic = library("ch.qos.logback:logback-classic");
    Library logback_core = library("ch.qos.logback:logback-core");
}
