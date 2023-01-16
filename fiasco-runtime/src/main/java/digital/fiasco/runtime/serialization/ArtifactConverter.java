package digital.fiasco.runtime.serialization;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.ensure.Ensure.illegalArgument;
import static com.telenav.kivakit.core.language.Classes.newInstance;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;

/**
 * Converts artifact notation to and from objects, where artifact notation is like "(library|asset)/[descriptor]", for
 * example, "library/com.telenav.kivakit:kivakit-core:1.8.5".
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("rawtypes")
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class ArtifactConverter extends BaseStringConverter<Artifact>
{
    public ArtifactConverter(Listener listener)
    {
        super(listener, Artifact.class);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts from the given {@link Artifact} instance to the notation "(library|asset)/[descriptor]", for example,
     * "library/com.telenav.kivakit:kivakit-core:1.8.5".
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    protected String onToString(Artifact artifact)
    {
        String type;
        if (artifact instanceof Library)
        {
            type = "library";
        }
        else if (artifact instanceof Asset)
        {
            type = "asset";
        }
        else
        {
            return illegalArgument("Unknown artifact type");
        }
        return type + "/" + artifact.name();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts from "(library|asset)/[descriptor]", such as "library/com.telenav.kivakit:kivakit-core:1.8.5", to the
     * appropriate {@link Artifact} instance.
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTING_INSUFFICIENT)
    protected Artifact<?> onToValue(String text)
    {
        var split = split(text, '/');
        var className = split.first();
        var descriptor = split.get(1);
        Class<? extends Artifact> type = switch (className)
            {
                case "library" -> Library.class;
                case "asset" -> Asset.class;
                default -> illegalArgument("Unknown artifact type: $", className);
            };

        return newInstance(type, ArtifactDescriptor.class, descriptor(descriptor));
    }
}
