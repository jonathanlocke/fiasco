package fiasco.repository;

import com.telenav.kivakit.interfaces.comparison.Filter;
import fiasco.Library;

import java.util.List;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface LibraryResolver
{
    List<Library> resolve(Library library, Filter<Library> exclusions);
}
