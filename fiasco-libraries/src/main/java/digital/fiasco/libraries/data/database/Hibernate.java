package digital.fiasco.libraries.data.database;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface Hibernate
{
    Library hibernate_core = library("org.hibernate:hibernate-core");
}
