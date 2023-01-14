package digital.fiasco.runtime.serialization;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;

import static com.telenav.kivakit.core.collections.list.StringList.splitOnPattern;
import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static digital.fiasco.runtime.dependency.artifact.ArtifactList.artifactList;

public class ArtifactListConverter extends BaseStringConverter<ArtifactList>
{
    public ArtifactListConverter(Listener listener)
    {
        super(listener, ArtifactList.class);
    }

    @Override
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

    @Override
    protected ArtifactList onToValue(String text)
    {
        var artifactConverter = new ArtifactConverter(this);
        var split = splitOnPattern(text, "\\s*,\\s*");
        var descriptors = split.rightOf(1);

        var dependencies = artifactList();
        for (var at : descriptors)
        {
            dependencies.add(artifactConverter.convert(at));
        }
        return dependencies;
    }
}
