package fiasco.metadata;

import com.telenav.kivakit.core.string.FormatProperty;
import com.telenav.kivakit.core.string.ObjectFormatter;

/**
 * Copyright text
 *
 * @author jonathan
 */
@SuppressWarnings({ "unused", "ClassCanBeRecord" })
public class Copyright
{
    @FormatProperty
    private final String text;

    /**
     * Creates a copyright with the given text
     *
     * @param text The copyright text
     */
    public Copyright(String text)
    {
        this.text = text;
    }

    public String text()
    {
        return text;
    }

    @Override
    public String toString()
    {
        return new ObjectFormatter(this).toString();
    }
}
