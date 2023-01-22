package digital.fiasco.runtime.repository.remote.serialization;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
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
     * Converts from the given {@link Artifact} instance to the notation "[type]:[group]:[artifact]:[version]", for
     * example, "library:com.telenav.kivakit:kivakit-core:1.8.5".
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    protected String onToString(Artifact artifact)
    {
        return artifact.name();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Converts from "[type]:[group]:[artifact]:[version]", such as "library:com.telenav.kivakit:kivakit-core:1.8.5", to
     * the appropriate {@link Artifact} instance.
     */
    @Override
    @MethodQuality(documentation = DOCUMENTED, testing = TESTING_INSUFFICIENT)
    protected Artifact<?> onToValue(String text)
    {
        var descriptor = descriptor(text);
        return newInstance(descriptor.type(), ArtifactDescriptor.class, descriptor);
    }
}
