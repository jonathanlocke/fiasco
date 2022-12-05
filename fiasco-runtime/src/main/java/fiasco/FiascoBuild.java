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

import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.code.Callback;

import static com.telenav.kivakit.core.ensure.Ensure.ensure;

@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused" })
public abstract class FiascoBuild extends Module
{
    private final BuildMetadata metadata;

    public FiascoBuild(BuildMetadata metadata)
    {
        super((FiascoBuild) null, (String) null);
        this.metadata = metadata;
        project(this);
    }

    public boolean build()
    {
        return build(Count._12);
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
        dependencies().process(this, threads, module -> module.builder().run());
        var statistics = issues.statistics(Problem.class, Warning.class, Quibble.class);
        information(statistics.titledBox("Build Results"));
        return issues.count(Problem.class).isZero();
    }

    public FiascoBuild module(String path)
    {
        return module(path, module ->
        {
        });
    }

    public FiascoBuild module(String path, Callback<Module> configure)
    {
        var folder = Folder.parseFolder(path);
        ensure(folder.path().isRelative());
        var module = new Module(this, folder);
        configure.call(module);
        requires(module);
        return this;
    }
}
