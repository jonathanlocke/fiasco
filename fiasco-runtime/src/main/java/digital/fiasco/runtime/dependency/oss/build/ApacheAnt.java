package digital.fiasco.runtime.dependency.oss.build;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings("unused")
public interface ApacheAnt
{
    Library apache_ant = library("org.apache.ant:ant");
}
