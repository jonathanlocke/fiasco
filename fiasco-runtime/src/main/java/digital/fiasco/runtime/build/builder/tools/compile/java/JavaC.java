package digital.fiasco.runtime.build.builder.tools.compile.java;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.value.count.Bytes;
import com.telenav.kivakit.filesystem.File;

public class JavaC
{
    private StringList arguments;

    private File javac;

    private Bytes initialMemory;

    private Bytes maximumMemory;
}
