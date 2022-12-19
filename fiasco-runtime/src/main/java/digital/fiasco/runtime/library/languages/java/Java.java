package digital.fiasco.runtime.library.languages.java;

import digital.fiasco.runtime.library.languages.java.io.Io;
import digital.fiasco.runtime.library.languages.java.collections.Collections;
import digital.fiasco.runtime.library.languages.java.concurrency.Concurrency;
import digital.fiasco.runtime.library.languages.java.javax.JavaX;
import digital.fiasco.runtime.library.languages.java.math.Math;
import digital.fiasco.runtime.library.languages.java.networking.Networking;
import digital.fiasco.runtime.library.languages.java.processes.Processes;
import digital.fiasco.runtime.library.languages.java.reflection.Reflection;
import digital.fiasco.runtime.library.languages.java.serialization.Serialization;
import digital.fiasco.runtime.library.languages.java.text.Text;
import digital.fiasco.runtime.library.languages.java.time.Time;
import digital.fiasco.runtime.library.languages.java.utilities.Utilities;

@SuppressWarnings({ "unused" })
public interface Java extends
        Collections,
        Concurrency,
        Io,
        JavaX,
        Math,
        Networking,
        Processes,
        Reflection,
        Serialization,
        Text,
        Time,
        Utilities

{
}
