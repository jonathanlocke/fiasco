package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Rooted;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public interface Build extends
        Repeater,
        Rooted,
        BuildStructure
{
    ArtifactDescriptor artifactDescriptor();

    @NotNull
    default String artifactName()
    {
        return artifactDescriptor().name();
    }

    ObjectList<BuildListener> buildListeners();

    boolean buildProject();

    boolean buildProject(Count threads);

    BaseBuild childBuild(String path);

    DependencyList dependencies();

    String description();

    void disable(Phase phase);

    boolean dryRun();

    void enable(Phase phase);

    Librarian librarian();

    /**
     * Returns the metadata for this build
     */
    BuildMetadata metadata();

    PhaseList phases();

    Build pinVersion(Artifact<?> artifact, String version);

    Build requires(Artifact<?> first, Artifact<?>... rest);

    Build requires(DependencyList dependencies);

    Build withArtifactDescriptor(ArtifactDescriptor descriptor);

    Build withArtifactDescriptor(String descriptor);

    Build withArtifactIdentifier(String identifier);

    Build withChildFolder(String child);

    Build withDependencies(Artifact<?>... dependencies);

    Build withDependencies(DependencyList dependencies);

    Build withRootFolder(Folder root);
}
