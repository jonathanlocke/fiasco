package digital.fiasco.runtime.build.builder.tools.toolchain.git;

import com.telenav.kivakit.conversion.core.time.BaseLocalTimeConverter;
import com.telenav.kivakit.core.messaging.Listener;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class GitLocalTimeConverter extends BaseLocalTimeConverter
{
    public GitLocalTimeConverter(Listener listener)
    {
        this(listener, ZoneId.systemDefault());
    }

    public GitLocalTimeConverter(Listener listener, ZoneId zone)
    {
        // Tue Feb 7 20:02:15 2023 -0700
        super(listener, DateTimeFormatter.ofPattern("EEE MMM d HH:mm:ss yyyy Z"), zone);
    }
}

