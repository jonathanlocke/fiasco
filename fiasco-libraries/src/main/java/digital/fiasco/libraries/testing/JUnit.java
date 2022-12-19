package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface JUnit
{
    Library junit = library("junit:junit");
    Library junit5_api = library("org.junit.jupiter:junit-jupiter-api");
    Library junit5_engine = library("org.junit.jupiter:junit-jupiter-engine");
}
