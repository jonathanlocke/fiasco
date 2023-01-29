package digital.fiasco.runtime.repository.remote.server.serialization.converters;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.collections.ArtifactList;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.StringList.splitOnPattern;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.messaging.Listener.throwingListener;

/**
 * Converts a comma-separated list of artifacts to and from the notation described in {@link ArtifactConverter}.
 *
 * @author Jonathan Locke
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class ArtifactListConverter extends BaseStringConverter<ArtifactList>
{
    public ArtifactListConverter()
    {
        super(throwingListener(), ArtifactList.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    protected String onToString(ArtifactList artifactList)
    {
        var artifactConverter = new ArtifactConverter<>(Artifact.class);
        var text = stringList();
        for (var artifact : artifactList)
        {
            text.add(artifactConverter.unconvert(artifact));
        }

        return text.join(", ");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    protected ArtifactList onToValue(String text)
    {
        var artifacts = ArtifactList.artifacts();

        if (!text.isBlank())
        {
            var artifactConverter = new ArtifactConverter<>(Artifact.class);
            var descriptors = splitOnPattern(text, "\\s*,\\s*");

            for (var at : descriptors)
            {
                artifacts = artifacts.with(artifactConverter.convert(at));
            }
        }

        return artifacts;
    }
}
