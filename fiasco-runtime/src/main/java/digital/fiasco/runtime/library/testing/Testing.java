package digital.fiasco.runtime.library.testing;

import static digital.fiasco.runtime.library.Library.library;

@SuppressWarnings({ "unused" })
public interface Testing extends
        AssertJ,
        EasyMock,
        GoogleFindBugs,
        Hamcrest,
        JUnit,
        Mockito,
        Powermock,
        TestNG,
        WireMock
{
}
