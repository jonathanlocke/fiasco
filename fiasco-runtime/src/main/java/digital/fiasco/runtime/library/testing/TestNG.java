package digital.fiasco.runtime.library.testing;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface TestNG
{
    Library test_ng = library("org.testng:testng");
}
