package digital.fiasco.runtime.serialization;

import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.converters.ResourceIdentifierConverter;
import com.telenav.kivakit.serialization.gson.factory.KivaKitCoreGsonFactory;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.ArtifactDescriptorConverter;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.fiasco.CacheRepository;
import digital.fiasco.runtime.repository.fiasco.LocalRepository;
import digital.fiasco.runtime.repository.fiasco.RemoteRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;

import static com.telenav.kivakit.conversion.core.time.TimeConverter.kivakitDateTimeConverter;

public class FiascoGsonFactory extends KivaKitCoreGsonFactory
{
    public FiascoGsonFactory(Listener listener)
    {
        super(listener);

        addConvertingSerializer(ArtifactAttachmentType.class, new EnumConverter<>(this, ArtifactAttachmentType.class));
        addConvertingSerializer(MavenRepository.class, new RepositoryConverter<>(this, MavenRepository.class));
        addConvertingSerializer(LocalRepository.class, new RepositoryConverter<>(this, LocalRepository.class));
        addConvertingSerializer(CacheRepository.class, new RepositoryConverter<>(this, CacheRepository.class));
        addConvertingSerializer(RemoteRepository.class, new RepositoryConverter<>(this, RemoteRepository.class));
        addConvertingSerializer(ArtifactDescriptor.class, new ArtifactDescriptorConverter(this));
        addConvertingSerializer(Time.class, kivakitDateTimeConverter(this));
        addConvertingSerializer(ArtifactList.class, new ArtifactListConverter(this));
        addConvertingSerializer(ResourceIdentifier.class, new ResourceIdentifierConverter(this));

        requireExposeAnnotation(true);
    }
}
