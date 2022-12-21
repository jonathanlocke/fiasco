package digital.fiasco.libraries.frameworks;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Osgi
{
    Library osgi_core = library("org.osgi:org.osgi.core");
}
