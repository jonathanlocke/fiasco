package digital.fiasco.runtime.serialization;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.converters.ResourceIdentifierConverter;
import com.telenav.kivakit.serialization.gson.factory.KivaKitCoreGsonFactory;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.ArtifactDescriptorConverter;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.fiasco.CacheRepository;
import digital.fiasco.runtime.repository.fiasco.LocalRepository;
import digital.fiasco.runtime.repository.fiasco.RemoteRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;

public class FiascoGsonFactory extends KivaKitCoreGsonFactory
{
    public FiascoGsonFactory(Listener listener)
    {
        super(listener);

        addConvertingSerializer(ArtifactAttachmentType.class, new EnumConverter<>(this, ArtifactAttachmentType.class));
        addConvertingSerializer(ArtifactDescriptor.class, new ArtifactDescriptorConverter(this));
        addConvertingSerializer(ArtifactList.class, new ArtifactListConverter(this));
        addConvertingSerializer(ResourceIdentifier.class, new ResourceIdentifierConverter(this));

        addTypeAdapterFactory(RuntimeTypeAdapterFactory
            .of(Repository.class, "type")
            .registerSubtype(MavenRepository.class)
            .registerSubtype(LocalRepository.class)
            .registerSubtype(CacheRepository.class)
            .registerSubtype(RemoteRepository.class));

        requireExposeAnnotation(true);
    }
}
