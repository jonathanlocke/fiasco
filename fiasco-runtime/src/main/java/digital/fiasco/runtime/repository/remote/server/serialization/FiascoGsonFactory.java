package digital.fiasco.runtime.repository.remote.server.serialization;

import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.annotations.code.quality.TypeQuality;
import com.telenav.kivakit.conversion.core.language.EnumConverter;
import com.telenav.kivakit.conversion.core.time.kivakit.KivaKitUtcTimeConverter;
import com.telenav.kivakit.resource.converters.ResourceIdentifierConverter;
import com.telenav.kivakit.serialization.gson.KivaKitCoreGsonFactory;
import com.telenav.kivakit.serialization.gson.serializers.primitive.ByteArrayGsonSerializer;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.content.ArtifactAttachmentType;
import digital.fiasco.runtime.dependency.artifact.types.Asset;
import digital.fiasco.runtime.dependency.artifact.types.Library;
import digital.fiasco.runtime.repository.Repository;
import digital.fiasco.runtime.repository.Repository.InstallationResult;
import digital.fiasco.runtime.repository.local.user.FiascoUserRepository;
import digital.fiasco.runtime.repository.local.cache.FiascoCacheRepository;
import digital.fiasco.runtime.repository.maven.MavenRepository;
import digital.fiasco.runtime.repository.remote.RemoteRepository;
import digital.fiasco.runtime.repository.remote.server.serialization.converters.ArtifactDescriptorConverter;
import digital.fiasco.runtime.repository.remote.server.serialization.converters.ArtifactDescriptorListConverter;
import digital.fiasco.runtime.repository.remote.server.serialization.converters.ArtifactListConverter;

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
        addSerializer(new EnumConverter<>(InstallationResult.class));
        addSerializer(new EnumConverter<>(ArtifactAttachmentType.class));
        addSerializer(new ArtifactDescriptorConverter());
        addSerializer(new ArtifactDescriptorListConverter());
        addSerializer(new ArtifactListConverter());
        addSerializer(new ResourceIdentifierConverter());
        addSerializer(new KivaKitUtcTimeConverter());
        addSerializer(new ByteArrayGsonSerializer());

        addGsonTypeAdapterFactory(RuntimeTypeAdapterFactory
            .of(Repository.class, "type")
            .registerSubtype(MavenRepository.class)
            .registerSubtype(FiascoUserRepository.class)
            .registerSubtype(FiascoCacheRepository.class)
            .registerSubtype(RemoteRepository.class));

        addGsonTypeAdapterFactory(RuntimeTypeAdapterFactory
            .of(Artifact.class, "type")
            .registerSubtype(Asset.class)
            .registerSubtype(Library.class));

        lenient(true);
        requireExposeAnnotation(true);
        prettyPrinting(true);
    }
}
