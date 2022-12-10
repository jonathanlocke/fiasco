package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;

/**
 * A compound matcher of {@link ResourcePathed} objects.
 *
 * @author jonathan
 */
public class Matchers implements Matcher<ResourcePathed>
{
    /** The files to copy */
    private ObjectList<Matcher<ResourcePathed>> matchers = list(matchAll());

    public Matchers add(Matcher<ResourcePathed> matcher)
    {
        matchers.add(matcher);
        return this;
    }

    public Matchers copy()
    {
        var copy = new Matchers();
        copy.matchers = matchers.copy();
        return copy;
    }

    @Override
    public boolean matches(ResourcePathed value)
    {
        for (var matcher : matchers)
        {
            if (matcher.matches(value))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString()
    {
        return matchers.join();
    }
}
