package digital.fiasco.runtime.serialization;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.core.collections.list.StringList.splitOnPattern;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;

/**
 * Converts a comma-separated list of artifacts to and from the notation described in {@link ArtifactConverter}.
 *
 * @author Jonathan Locke
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class ArtifactListConverter extends BaseStringConverter<ArtifactList>
{
    public ArtifactListConverter(Listener listener)
    {
        super(listener, ArtifactList.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    protected String onToString(ArtifactList artifactList)
    {
        var artifactConverter = new ArtifactConverter(this);
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
        var artifactConverter = new ArtifactConverter(this);
        var split = splitOnPattern(text, "\\s*,\\s*");
        var descriptors = split.rightOf(1);

        var artifacts = ArtifactList.artifacts();
        for (var at : descriptors)
        {
            artifacts = artifacts.with(artifactConverter.convert(at));
        }
        return artifacts;
    }
}
