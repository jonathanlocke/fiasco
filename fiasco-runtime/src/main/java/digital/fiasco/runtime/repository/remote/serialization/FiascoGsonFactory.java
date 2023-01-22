package digital.fiasco.runtime.repository.remote.serialization;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitUtcTimeConverter;
import com.telenav.kivakit.resource.converters.ResourceIdentifierConverter;
import com.telenav.kivakit.serialization.gson.KivaKitCoreGsonFactory;
import digital.fiasco.runtime.dependency.artifact.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor.ArtifactDescriptorConverter;
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
    public FiascoGsonFactory()
    {
        addSerializer(new EnumConverter<>(ArtifactAttachmentType.class));
        addSerializer(new ArtifactDescriptorConverter());
        addSerializer(new ArtifactListConverter());
        addSerializer(new ResourceIdentifierConverter());
        addSerializer(new KivaKitUtcTimeConverter());

        addGsonTypeAdapterFactory(RuntimeTypeAdapterFactory
            .of(Repository.class, "type")
            .registerSubtype(MavenRepository.class)
            .registerSubtype(LocalRepository.class)
            .registerSubtype(CacheRepository.class)
            .registerSubtype(RemoteRepository.class));

        requireExposeAnnotation(true);
    }
}