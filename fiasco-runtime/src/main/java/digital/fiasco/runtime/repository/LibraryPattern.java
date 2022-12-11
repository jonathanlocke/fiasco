package digital.fiasco.runtime.repository;

import com.telenav.kivakit.interfaces.comparison.Matcher;

/**
 * @author jonathan
 */
public class LibraryPattern implements Matcher<Library>
{
    @Override
    public boolean matches(final Library library)
    {
        return false;
    }
}
