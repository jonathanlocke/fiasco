package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface GoogleFindBugs
{
    Library find_bugs = library("com.google.code.findbugs:jsr305");
}
