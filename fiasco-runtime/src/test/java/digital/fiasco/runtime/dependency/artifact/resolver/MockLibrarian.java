package digital.fiasco.runtime.dependency.artifact.resolver;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.version.Version;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactDescriptorList;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.librarian.Librarian;

import static digital.fiasco.runtime.dependency.collections.lists.ArtifactList.artifacts;

public class MockLibrarian extends BaseComponent implements Librarian
{
    @Override
    public Librarian copy()
    {
        return this;
    }

    @Override
    public String description()
    {
        return "Mock librarian";
    }

    @Override
    public ArtifactList resolve(ArtifactDescriptorList descriptors)
    {
        trace("Mock librarian resolving: $", descriptors);
        return descriptors.asArtifacts();
    }

    @Override
    public ArtifactList resolve(Artifact<?> artifact)
    {
        return artifacts(artifact);
    }

    @Override
    public Librarian withPinnedVersion(ArtifactDescriptor descriptor, Version version)
    {
        return this;
    }
}
