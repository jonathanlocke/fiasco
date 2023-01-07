package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Year;

import static com.telenav.kivakit.core.time.Year.year;

/**
 * Copyright text
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused" })
public record Copyright(@FormatProperty String text,
                        @FormatProperty Year from,
                        @FormatProperty Year to)
{
    public static Copyright copyright()
    {
        return new Copyright(null, null, null);
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }

    public Copyright withFrom(int from)
    {
        return new Copyright(text, year(from), to);
    }

    public Copyright withText(String text)
    {
        return new Copyright(text, from, to);
    }

    public Copyright withTo(int to)
    {
        return new Copyright(text, from, year(to));
    }

    public Copyright withYears(int from, int to)
    {
        return new Copyright(text, year(from), year(to));
    }
}
