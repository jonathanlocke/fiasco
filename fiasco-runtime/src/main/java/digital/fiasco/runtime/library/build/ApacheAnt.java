package digital.fiasco.runtime.library.build;

import digital.fiasco.runtime.library.Library;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings("unused")
public interface ApacheAnt
{
    Library apache_ant = library("org.apache.ant:ant");
}
