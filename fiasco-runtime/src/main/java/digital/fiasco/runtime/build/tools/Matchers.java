package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;

/**
 * A compound matcher of {@link ResourcePathed} objects.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
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
        return matchers.map(this::toString)
            .join()
            .replaceAll("\\$", ".");
    }

    @NotNull
    private String toString(Matcher<ResourcePathed> at)
    {
        var text = at.toString();
        var matcher = Pattern.compile("([A-Za-z0-9_]+)\\$\\$").matcher(text);
        if (matcher.find())
        {
            text = "(" + matcher.group(1) + " lambda)";
        }
        return text;
    }
}
