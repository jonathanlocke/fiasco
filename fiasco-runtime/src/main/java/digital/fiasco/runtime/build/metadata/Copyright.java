package digital.fiasco.runtime.build.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;
import com.telenav.kivakit.core.time.Year;

/**
 * Copyright text
 *
 * @author jonathan
 */
@SuppressWarnings({ "unused", "ClassCanBeRecord" })
public record Copyright(@FormatProperty String text,
                        @FormatProperty Year from,
                        @FormatProperty Year to)
{
    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
