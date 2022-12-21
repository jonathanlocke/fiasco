package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface TestNG
{
    Library test_ng = library("org.testng:testng");
}
