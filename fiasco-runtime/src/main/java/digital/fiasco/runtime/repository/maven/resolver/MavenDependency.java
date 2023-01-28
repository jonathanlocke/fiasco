package digital.fiasco.runtime.repository.maven.resolver;

import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.repository.maven.MavenRepository;
import org.jetbrains.annotations.NotNull;

/**
 * The desriptor for a Maven dependency and the repository where it can be found
 *
 * @param repository The hosting repository
 * @param descriptor The artifact descriptor
 */
public record MavenDependency(@NotNull MavenRepository repository,
                              @NotNull ArtifactDescriptor descriptor)
{
    @Override
    public String toString()
    {
        return repository.name() + " => " + descriptor;
    }
}
