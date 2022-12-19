package digital.fiasco.runtime.library.frameworks;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface Osgi
{
    Library osgi_core = library("org.osgi:org.osgi.core");
}
