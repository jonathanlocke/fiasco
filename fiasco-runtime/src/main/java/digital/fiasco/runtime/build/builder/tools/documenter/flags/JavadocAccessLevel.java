package digital.fiasco.runtime.build.builder.tools.documenter.flags;

import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.interfaces.object.Copyable;

import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.ensure;

public class JavadocAccessLevel implements Copyable<JavadocAccessLevel>
{
    public static JavadocAccessLevel packageAccessLevel()
    {
        return new JavadocAccessLevel("protected");
    }

    public static JavadocAccessLevel privateAccessLevel()
    {
        return new JavadocAccessLevel("private");
    }

    public static JavadocAccessLevel protectedAccessLevel()
    {
        return new JavadocAccessLevel("package");
    }

    public static JavadocAccessLevel publicAccessLevel()
    {
        return new JavadocAccessLevel("public");
    }

    /** The access level, one of public, package, protected or private */
    private final String accessLevel;

    /** The warnings to enable */
    private ObjectSet<JavadocWarning> enabledWarnings = set();

    JavadocAccessLevel(String flag)
    {
        this.accessLevel = flag;
    }

    JavadocAccessLevel(JavadocAccessLevel that)
    {
        this.accessLevel = that.accessLevel;
        this.enabledWarnings = that.enabledWarnings.copy();
    }

    public String accessLevelName()
    {
        return accessLevel;
    }

    public void checkConsistency()
    {
        ensure(enabledWarnings.equals(set(JavadocWarning.NONE))
                || enabledWarnings.equals(set(JavadocWarning.ALL))
                || !enabledWarnings.isEmpty(),
            "Debug information for access level '$' must be NONE, ALL, "
                + "or any combination of SOURCE_FILES, LINES, and VARIABLES", accessLevelName());
    }

    @Override
    public JavadocAccessLevel copy()
    {
        return new JavadocAccessLevel(this);
    }

    public JavadocAccessLevel withWarnings(JavadocWarning... warnings)
    {
        return mutatedCopy(it -> it.enabledWarnings = enabledWarnings.with(warnings));
    }

    public JavadocAccessLevel withoutWarnings(JavadocWarning... warnings)
    {
        return mutatedCopy(it -> it.enabledWarnings = enabledWarnings.without(warnings));
    }
}
