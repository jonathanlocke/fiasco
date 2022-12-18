package digital.fiasco.runtime.dependency.oss.database;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Hibernate
{
    Library hibernate_core = library("org.hibernate:hibernate-core");
}
