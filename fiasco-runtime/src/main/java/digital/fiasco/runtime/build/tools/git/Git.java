//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.tools.git;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.BuildAssociated;
import digital.fiasco.runtime.build.tools.BaseTool;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.string.Formatter.format;

/**
 * Runs git
 *
 * @author shibo
 */
@SuppressWarnings("unused")
public class Git extends BaseTool
{
    /** The folder where git should be run */
    Folder folder;

    /** The arguments to pass to git */
    StringList arguments = stringList();

    private String output;

    public Git(BuildAssociated build)
    {
        super(build.associatedBuild());
        folder = build.associatedBuild().rootFolder();
    }

    public Git(Git that)
    {
        super(that.associatedBuild());
        this.folder = that.folder;
        this.arguments = that.arguments.copy();
        this.output = that.output;
    }

    public Git commitHash()
    {
        return withArguments("git", "rev-parse", "HEAD");
    }

    public Git commitTime()
    {
        // Mon Dec 5 03:49:27 2022 -0700
        return withArguments("git", "log", "-1", "--format=%cd");
    }

    public Git copy()
    {
        return new Git(this);
    }

    public String output()
    {
        return output;
    }

    public Git withArguments(String... arguments)
    {
        var copy = copy();
        copy.arguments = stringList(arguments);
        return copy;
    }

    public Git withFolder(Folder folder)
    {
        var copy = copy();
        copy.folder = folder;
        return copy;
    }

    @Override
    protected String description()
    {
        return format("""
                Git
                  folder: $
                  arguments: $
                """, folder, stringList(arguments).join(" "));
    }

    @Override
    protected void onRun()
    {
        information(description());
        output = operatingSystem().execute(this,
                folder.asJavaFile(), arguments.asStringArray());
    }
}
