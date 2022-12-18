package digital.fiasco.runtime.dependency.oss.testing;

import static digital.fiasco.runtime.repository.Library.library;

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
