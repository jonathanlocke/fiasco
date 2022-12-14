package digital.fiasco.runtime.dependency.oss;

import digital.fiasco.runtime.repository.Library;

import static digital.fiasco.runtime.repository.Library.library;

@SuppressWarnings({ "unused" })
public interface Build
{
    Library apache_ant = library("org.apache.ant:ant");
    Library gradle = library("com.android.tools.build:gradle");
}
