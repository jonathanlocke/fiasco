package digital.fiasco.runtime.serialization;

import com.telenav.kivakit.conversion.BaseStringConverter;
import com.telenav.kivakit.core.messaging.Listener;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.Asset;
import digital.fiasco.runtime.dependency.artifact.Library;

import static com.telenav.kivakit.core.collections.list.StringList.split;
import static com.telenav.kivakit.core.ensure.Ensure.illegalArgument;
import static com.telenav.kivakit.core.language.Classes.newInstance;
import static digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.descriptor;

@SuppressWarnings("rawtypes")
public class ArtifactConverter extends BaseStringConverter<Artifact>
{
    protected ArtifactConverter(Listener listener)
    {
        super(listener, Artifact.class);
    }

    @Override
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

    @Override
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

        return newInstance(type, descriptor(descriptor));
    }
}
