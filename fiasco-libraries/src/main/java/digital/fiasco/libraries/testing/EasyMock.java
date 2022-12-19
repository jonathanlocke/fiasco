package digital.fiasco.libraries.testing;

import digital.fiasco.runtime.dependency.library.Library;

import static digital.fiasco.runtime.dependency.library.Library.library;

@SuppressWarnings("unused")
public interface EasyMock
{
    Library easymock = library("org.easymock:easymock");
}
