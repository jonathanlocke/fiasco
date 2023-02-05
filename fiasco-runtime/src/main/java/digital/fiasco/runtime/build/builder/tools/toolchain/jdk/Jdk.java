package digital.fiasco.runtime.build.builder.tools.toolchain.jdk;

import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.object.Copyable;
import org.jetbrains.annotations.NotNull;

public class Jdk implements Copyable<Jdk>
{
    public static Jdk jdk(Version version)
    {
        return new Jdk(version);
    }

    public static Jdk jdk(String version)
    {
        return new Jdk(Version.version(version));
    }

    private final Version version;

    private Folder home;

    protected Jdk(Jdk that)
    {
        this.version = that.version;
        this.home = that.home;
    }

    private Jdk(Version version)
    {
        this.version = version;
    }

    @Override
    public Jdk copy()
    {
        return new Jdk(this);
    }

    @Override
    public boolean equals(Object object)
    {
        if (object instanceof Jdk that)
        {
            return this.version.equals(that.version);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return version.hashCode();
    }

    public Folder home()
    {
        return home;
    }

    public File java()
    {
        return home.file("java");
    }

    public File javac()
    {
        return home.file("javac");
    }

    @NotNull
    public String name()
    {
        return "JDK " + version;
    }

    @Override
    public String toString()
    {
        return name();
    }

    public Version version()
    {
        return version;
    }

    public Jdk withHomeFolder(Folder home)
    {
        return mutatedCopy(it -> it.home = home);
    }
}
