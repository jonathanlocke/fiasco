package digital.fiasco.runtime.repository.remote.server.serialization.converters;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonElementSerializer;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

public class ArtifactDescriptorConverter extends BaseGsonElementSerializer<ArtifactDescriptor>
{
    public ArtifactDescriptorConverter()
    {
        super(ArtifactDescriptor.class);
    }

    @Override
    protected JsonElement toJson(ArtifactDescriptor value)
    {
        var json = new JsonObject();
        json.addProperty("name", value.name());
        return json;
    }

    @Override
    protected ArtifactDescriptor toValue(JsonElement object)
    {
        var json = (JsonObject) object;
        return ArtifactDescriptor.descriptor(json.get("name").getAsString());
    }
}
