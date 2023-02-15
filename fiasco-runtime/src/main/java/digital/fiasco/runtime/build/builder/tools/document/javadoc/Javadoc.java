package digital.fiasco.runtime.build.builder.tools.document.javadoc;

import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseFileTool;
import digital.fiasco.runtime.build.builder.tools.document.javadoc.flags.JavadocAccessLevel;
import digital.fiasco.runtime.build.builder.tools.document.javadoc.flags.JavadocWarning;

import static com.telenav.kivakit.core.collections.map.ObjectMap.map;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.string.Formatter.format;

public class Javadoc extends BaseFileTool<Javadoc, Void>
{
    /** The Javadoc warnings that are enabled, per access level */
    private ObjectMap<String, JavadocAccessLevel> accessLevels = map();

    public Javadoc(Builder builder)
    {
        super(builder);
    }

    public Javadoc(Javadoc that)
    {
        super(that);
        this.accessLevels = that.accessLevels.copy();
    }

    /**
     * Returns the set of Javadoc warnings that are enabled
     *
     * @return The set of warnings
     */
    public ObjectSet<JavadocAccessLevel> accessLevels()
    {
        return set(accessLevels.values());
    }

    @Override
    public void checkConsistency()
    {
        accessLevels().forEach(JavadocAccessLevel::checkConsistency);
    }

    /**
     * Returns a copy of this tool
     */
    @Override
    public Javadoc copy()
    {
        return new Javadoc(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return format("""
            Compiler
              sources: $
              enabled warnings: $
            """, files(), accessLevels().join("\n"));
    }

    @Override
    public Void onRun()
    {
        return null;
    }

    /**
     * Returns a copy of this compiler tool with the given Javadoc warnings disabled
     *
     * @param warnings The Javadoc warnings
     * @return The new copy of this compiler tool
     */
    public Javadoc withDisabled(JavadocAccessLevel level, JavadocWarning... warnings)
    {
        return mutated(it -> it.accessLevels.get(level.accessLevelName()).withoutWarnings(warnings));
    }

    /**
     * Returns a copy of this compiler tool with the given Javadoc warnings disabled
     *
     * @param warnings The Javadoc warnings
     * @return The new copy of this compiler tool
     */
    public Javadoc withEnabled(JavadocAccessLevel level, JavadocWarning... warnings)
    {
        return mutated(it -> it.accessLevels.get(level.accessLevelName()).withWarnings(warnings));
    }
}
