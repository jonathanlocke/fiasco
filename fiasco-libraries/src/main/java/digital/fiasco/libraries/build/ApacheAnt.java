package digital.fiasco.libraries.build;

import digital.fiasco.runtime.dependency.artifact.Library;

import static digital.fiasco.runtime.dependency.artifact.Library.library;

@SuppressWarnings("unused")
public interface ApacheAnt
{
    Library apache_ant = library("org.apache.ant:ant");
}
