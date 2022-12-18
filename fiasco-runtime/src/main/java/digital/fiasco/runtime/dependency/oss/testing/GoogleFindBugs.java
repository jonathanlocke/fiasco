package digital.fiasco.runtime.dependency.oss.testing;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface GoogleFindBugs
{
    Library find_bugs = library("com.google.code.findbugs:jsr305");
}
