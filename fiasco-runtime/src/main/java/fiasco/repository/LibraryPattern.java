package fiasco.repository;

import com.telenav.kivakit.interfaces.comparison.Matcher;
import fiasco.Library;

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
