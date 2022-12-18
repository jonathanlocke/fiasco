package digital.fiasco.runtime.dependency.oss.testing;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface JUnit
{
    Library junit = library("junit:junit");
    Library junit5_api = library("org.junit.jupiter:junit-jupiter-api");
    Library junit5_engine = library("org.junit.jupiter:junit-jupiter-engine");
}
