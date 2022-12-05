//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package fiasco;

import com.telenav.kivakit.component.BaseComponent;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;
import fiasco.dependency.DependencyList;
import fiasco.environment.EnvironmentTrait;
import fiasco.glob.Glob;
import fiasco.metadata.Metadata;
import fiasco.repository.artifact.Artifact;
import fiasco.structure.StructureMixin;
import fiasco.tools.builder.BuildListener;

import java.util.function.Consumer;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static fiasco.repository.artifact.Artifact.parseArtifact;

@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused" })
public abstract class BaseBuild extends BaseComponent implements
        ToolFactory,
        Glob,
        EnvironmentTrait,
        StructureMixin,
        BuildAttached
{
    private Metadata metadata;

    private final ObjectList<BuildListener> buildListeners = list();

    private final DependencyList<Library> libraries = new DependencyList<>();

    private Artifact artifact;

    public void addListener(BuildListener listener)
    {
        buildListeners.add(listener);
    }

    public Artifact artifact()
    {
        return artifact;
    }

    @Override
    public BaseBuild baseBuild()
    {
        return this;
    }

    public boolean build()
    {
        return build(processors());
    }

    /**
     * Builds this project with the given number of worker threads
     *
     * @return True if the build succeeded without any problems
     */
    @SuppressWarnings("unchecked")
    public boolean build(Count threads)
    {
        var issues = new MessageList(message -> !message.status().succeeded());
        var statistics = issues.statistics(Problem.class, Warning.class, Quibble.class);
        information(statistics.titledBox("Build Results"));
        return issues.count(Problem.class).isZero();
    }

    public DependencyList<Library> libraries()
    {
        return libraries;
    }

    public void metadata(Metadata metadata)
    {
        this.metadata = metadata;
    }

    public Metadata metadata()
    {
        return metadata;
    }

    public void notify(Consumer<BuildListener> consumer)
    {
        buildListeners.forEach(consumer);
    }

    public ToolFactory requires(Library library)
    {
        libraries.add(library);
        return this;
    }

    protected void artifact(String descriptor)
    {
        artifact(parseArtifact(descriptor));
    }

    protected void artifact(Artifact artifact)
    {
        this.artifact = artifact;
    }
}
