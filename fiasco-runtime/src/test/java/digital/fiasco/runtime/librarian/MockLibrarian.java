package digital.fiasco.runtime.librarian;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.version.Version;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.descriptor.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;

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
    public ArtifactList resolve(ObjectList<ArtifactDescriptor> descriptors)
    {
        trace("Mock librarian resolving: $", descriptors);
        return artifacts(descriptors.map(ArtifactDescriptor::asArtifact));
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
