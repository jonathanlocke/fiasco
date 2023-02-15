package digital.fiasco.runtime.repository.remote.server.serialization.converters;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTING_INSUFFICIENT;
import static com.telenav.kivakit.core.language.Classes.newInstance;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.artifactDescriptor;

/**
 * Converts artifact notation to and from objects, where artifact notation is like "(library|asset)/[descriptor]", for
 * example, "library/com.telenav.kivakit:kivakit-core:1.8.5".
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class ArtifactConverter<T extends Artifact> extends BaseStringConverter<T>
{
    public ArtifactConverter(Listener listener, Class<T> type)
    {
        super(listener, type);
    }

    public ArtifactConverter(Class<T> type)
    {
        this(throwingListener(), type);
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
    protected T onToValue(String text)
    {
        var descriptor = artifactDescriptor(text);
        return (T) newInstance(descriptor.type(), ArtifactDescriptor.class, descriptor);
    }
}
