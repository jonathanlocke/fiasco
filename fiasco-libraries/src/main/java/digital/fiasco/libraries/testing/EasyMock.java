package digital.fiasco.libraries.testing;

import digital.fiasco.libraries.LibraryGroups;
import digital.fiasco.runtime.dependency.artifact.types.Library;

@SuppressWarnings("unused")
public interface EasyMock extends LibraryGroups
{
    Library easymock = easymock_group.library("easymock").asLibrary();
}
