package digital.fiasco.runtime.repository.remote.server.serialization.converters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.telenav.kivakit.serialization.gson.serializers.BaseGsonElementSerializer;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptorList;

import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor.artifactDescriptor;
import static digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptorList.descriptors;

public class ArtifactDescriptorListConverter extends BaseGsonElementSerializer<ArtifactDescriptorList>
{
    public ArtifactDescriptorListConverter()
    {
        super(ArtifactDescriptorList.class);
    }

    @Override
    protected JsonElement toJson(ArtifactDescriptorList value)
    {
        var json = new JsonArray();
        for (var at : value)
        {
            json.add(at.name());
        }
        return json;
    }

    @Override
    protected ArtifactDescriptorList toValue(JsonElement object)
    {
        var json = (JsonArray) object;
        var descriptors = descriptors();
        for (var at : json)
        {
            descriptors = descriptors.with(artifactDescriptor(at.getAsString()));
        }
        return descriptors;
    }
}
