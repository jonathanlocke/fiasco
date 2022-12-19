package digital.fiasco.runtime.library.testing;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface GoogleFindBugs
{
    Library find_bugs = library("com.google.code.findbugs:jsr305");
}
