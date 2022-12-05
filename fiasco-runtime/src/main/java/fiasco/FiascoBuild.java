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
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.code.Callback;
import fiasco.dependency.DependencyList;
import fiasco.environment.EnvironmentTrait;
import fiasco.glob.Glob;
import fiasco.metadata.Metadata;
import fiasco.repository.artifact.Artifact;
import fiasco.structure.StructureMixin;
import fiasco.tools.Tools;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused" })
public abstract class FiascoBuild extends BaseComponent implements
        Tools,
        Glob,
        EnvironmentTrait,
        StructureMixin
{
    private Metadata metadata;

    private final DependencyList<Library> libraries = new DependencyList<>();

    private Artifact artifact;

    public Artifact artifact()
    {
        return artifact;
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
        libraries().process(this, threads, module -> module.builder().run());
        var statistics = issues.statistics(Problem.class, Warning.class, Quibble.class);
        information(statistics.titledBox("Build Results"));
        return issues.count(Problem.class).isZero();
    }

    public Folder classesFolder()
    {
        return outputFolder().folder("classes");
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

    public FiascoBuild module(String path)
    {
        return module(path, module ->
        {
        });
    }

    public FiascoBuild module(String path, Callback<Tools> configure)
    {
        var folder = Folder.parseFolder(path);
        ensure(folder.path().isRelative());
        var module = new Tools(this, folder);
        configure.call(module);
        requires(module);
        return this;
    }

    public Tools requires(Library library)
    {
        libraries.add(library);
        return this;
    }

    protected void artifact(String descriptor)
    {
        artifact(Artifact.parse(descriptor));
    }

    protected void artifact(Artifact artifact)
    {
        this.artifact = artifact;
    }
}
