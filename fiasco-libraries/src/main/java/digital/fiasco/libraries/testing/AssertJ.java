package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface AssertJ
{
    Library assertj_core = library("org.assertj:assertj-core");
}
