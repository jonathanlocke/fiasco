package digital.fiasco.runtime.dependency.oss.frameworks;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Osgi
{
    Library osgi_core = library("org.osgi:org.osgi.core");
}
