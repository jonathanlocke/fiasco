package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.artifact.types.Library;

import static digital.fiasco.runtime.dependency.artifact.types.Library.library;

@SuppressWarnings("unused")
public interface GoogleFindBugs
{
    Library find_bugs = library("com.google.code.findbugs:jsr305");
}
