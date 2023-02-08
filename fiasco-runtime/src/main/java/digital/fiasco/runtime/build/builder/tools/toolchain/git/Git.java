//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.builder.tools.toolchain.git;

import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.string.Formatter.format;

/**
 * Runs the Git source control tool.
 *
 * <p><b>Factory Methods</b></p>
 *
 * <ul>
 *     <li>{@link #commitHash()} - Returns a Git tool that gets the most recent commit hash</li>
 *     <li>{@link #commitTime()} - Returns a Git tool that gets the most recent commit time</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #arguments()}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #withArguments(String...)}</li>
 *     <li>{@link #withWorkingFolder(Folder)}</li>
 * </ul>
 *
 * <p><b>Execution</b></p>
 *
 * <ul>
 *     <li>{@link #run()}</li>
 *     <li>{@link #output()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Git extends BaseTool<Git>
{
    /** The folder where git should be run */
    Folder folder;

    /** The arguments to pass to git */
    StringList arguments = stringList();

    /** The output captured from running this tool */
    private String output;

    /**
     * Creates a Git tool associated with the given builder
     *
     * @param builder The builder
     */
    public Git(Builder builder)
    {
        super(builder);
        folder = builder.rootFolder();
    }

    /**
     * Creates a copy of the given Git tool
     *
     * @param that The Git tool to copy
     */
    public Git(Git that)
    {
        super(that.associatedBuilder());
        this.folder = that.folder;
        this.arguments = that.arguments.copy();
        this.output = that.output;
    }

    /**
     * Returns the command line arguments to Git
     */
    public StringList arguments()
    {
        return arguments;
    }

    /**
     * Returns a Git tool that gets the most recent commit hash
     */
    public Git commitHash()
    {
        return withArguments("git", "rev-parse", "HEAD");
    }

    /**
     * Returns a Git tool that gets the most recent commit time
     */
    public Git commitTime()
    {
        // Mon Dec 5 03:49:27 2022 -0700
        return withArguments("git", "log", "-1", "--format=%cd");
    }

    /**
     * Returns a copy of this Git tool
     */
    @Override
    public Git copy()
    {
        return new Git(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return format("""
            Git
              folder: $
              arguments: $
            """, folder, stringList(arguments).join(" "));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRun()
    {
        information(description());
        output = operatingSystem().execute(this,
            folder.asJavaFile(), arguments.asStringArray()).trim();
    }

    /**
     * Returns the output from running this tool
     */
    public String output()
    {
        return output;
    }

    /**
     * Returns this Git tool with the given arguments
     *
     * @param arguments The arguments
     * @return A copy of this Git tool with the given arguments
     */
    public Git withArguments(String... arguments)
    {
        var copy = copy();
        copy.arguments = stringList(arguments);
        return copy;
    }

    /**
     * Returns this Git tool with the given working folder
     *
     * @param folder The working folder
     * @return A copy of this Git tool with the given working folder
     */
    public Git withWorkingFolder(Folder folder)
    {
        var copy = copy();
        copy.folder = folder;
        return copy;
    }
}
