package digital.fiasco.libraries.build;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheAnt
{
    Library apache_ant = library("org.apache.ant:ant");
}
