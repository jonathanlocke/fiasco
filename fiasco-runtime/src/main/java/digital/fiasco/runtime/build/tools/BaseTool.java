package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.repeaters.BaseRepeater;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildListener;
import digital.fiasco.runtime.build.BuildMetadata;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import static com.telenav.kivakit.core.ensure.Ensure.unsupported;

/**
 * Base class for {@link Tool}s.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public abstract class BaseTool extends BaseRepeater implements Tool
{
    private final Build build;

    public BaseTool(Build build)
    {
        this.build = build;
        build.addListener(this);
    }

    @Override
    public ArtifactDescriptor artifactDescriptor()
    {
        return build.artifactDescriptor();
    }

    @Override
    public ObjectList<BuildListener> buildListeners()
    {
        return build.buildListeners();
    }

    @Override
    public boolean buildProject()
    {
        return build.buildProject();
    }

    @Override
    public boolean buildProject(final Count threads)
    {
        return build.buildProject();
    }

    @Override
    public BaseBuild childBuild(final String path)
    {
        return build.childBuild(path);
    }

    @Override
    public DependencyList dependencies()
    {
        return build.dependencies();
    }

    @Override
    public String description()
    {
        return build.description();
    }

    @Override
    public void disable(final Phase phase)
    {
    }

    @Override
    public boolean dryRun()
    {
        return false;
    }

    @Override
    public void enable(final Phase phase)
    {
    }

    @Override
    public Librarian librarian()
    {
        return build.librarian();
    }

    @Override
    public BuildMetadata metadata()
    {
        return build.metadata();
    }

    @Override
    public PhaseList phases()
    {
        return build.phases();
    }

    @Override
    public Build pinVersion(final Artifact<?> artifact, final String version)
    {
        return build.pinVersion(artifact, version);
    }

    @Override
    public Build requires(final Artifact<?> first, final Artifact<?>... rest)
    {
        return build.requires(first, rest);
    }

    @Override
    public Build requires(final DependencyList dependencies)
    {
        return build.requires(dependencies);
    }

    @Override
    public Folder rootFolder()
    {
        return build.rootFolder();
    }

    @Override
    public final void run()
    {
        if (describe())
        {
            onDescribe();
        }
        else
        {
            onRunning();
            onRun();
            onRan();
        }
    }

    @Override
    public Build withArtifactDescriptor(final ArtifactDescriptor descriptor)
    {
        return unsupported();
    }

    @Override
    public Build withArtifactDescriptor(final String descriptor)
    {
        return unsupported();
    }

    @Override
    public Build withArtifactIdentifier(final String identifier)
    {
        return unsupported();
    }

    @Override
    public Build withChildFolder(final String child)
    {
        return unsupported();
    }

    @Override
    public Build withDependencies(final Artifact<?>... dependencies)
    {
        return unsupported();
    }

    @Override
    public Build withDependencies(final DependencyList dependencies)
    {
        return unsupported();
    }

    @Override
    public Build withRootFolder(final Folder root)
    {
        return unsupported();
    }

    protected boolean describe()
    {
        return build.dryRun();
    }

    protected void onDescribe()
    {
        announce(" \n" + description());
    }

    protected void onRan()
    {
    }

    protected abstract void onRun();

    protected void onRunning()
    {
    }
}
