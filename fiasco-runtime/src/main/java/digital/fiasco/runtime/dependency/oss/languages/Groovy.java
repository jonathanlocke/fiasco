package digital.fiasco.runtime.dependency.oss.languages;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface Groovy
{
    Library groovy_all = library("org.codehaus.groovy:groovy-all");
}
