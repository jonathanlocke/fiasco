package digital.fiasco.runtime.serialization;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitUtcTimeConverter;
import com.telenav.kivakit.core.messaging.Listener;
import com.telenav.kivakit.core.time.Time;
import com.telenav.kivakit.resource.ResourceIdentifier;
import com.telenav.kivakit.resource.converters.ResourceIdentifierConverter;
import com.telenav.kivakit.serialization.gson.factory.KivaKitCoreGsonFactory;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.ArtifactDescriptorConverter;
import digital.fiasco.runtime.dependency.artifact.ArtifactList;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.local.CacheRepository;
import digital.fiasco.runtime.repository.local.LocalRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;
import digital.fiasco.runtime.repository.remote.RemoteRepository;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTATION_NOT_NEEDED;
import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Stability.STABLE;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;

/**
 * Adds Gson serialization support to {@link KivaKitCoreGsonFactory} for Fiasco objects.
 *
 * @author Jonathan Locke
 */
@TypeQuality(documentation = DOCUMENTED, testing = TESTED, stability = STABLE)
public class FiascoGsonFactory extends KivaKitCoreGsonFactory
{
    @MethodQuality(documentation = DOCUMENTATION_NOT_NEEDED, testing = TESTED)
    public FiascoGsonFactory(Listener listener)
    {
        super(listener);

        addConvertingSerializer(ArtifactAttachmentType.class, new EnumConverter<>(this, ArtifactAttachmentType.class));
        addConvertingSerializer(ArtifactDescriptor.class, new ArtifactDescriptorConverter(this));
        addConvertingSerializer(ArtifactList.class, new ArtifactListConverter(this));
        addConvertingSerializer(ResourceIdentifier.class, new ResourceIdentifierConverter(this));
        addConvertingSerializer(Time.class, new KivaKitUtcTimeConverter());

        addTypeAdapterFactory(RuntimeTypeAdapterFactory
            .of(Repository.class, "type")
            .registerSubtype(MavenRepository.class)
            .registerSubtype(LocalRepository.class)
            .registerSubtype(CacheRepository.class)
            .registerSubtype(RemoteRepository.class));

        requireExposeAnnotation(true);
    }
}
