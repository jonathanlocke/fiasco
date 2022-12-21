package digital.fiasco.runtime.repository.maven.resolver;

import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.repository.maven.MavenRepository;
import org.jetbrains.annotations.NotNull;

public record MavenDependency(@NotNull MavenRepository repository,
                              @NotNull ArtifactDescriptor descriptor
)
{
    public String toString()
    {
        return repository.name() + " => " + descriptor;
    }
}
