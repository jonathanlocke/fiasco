//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.tools.copier;

import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.ResourcePathed;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.build.tools.Matchers;

import static com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter.progressReporter;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.resource.ResourceGlob.glob;

/**
 * Copies selected files from one folder to another.
 *
 * @author shibo
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Copier extends BaseTool
{
    /** The folder to copy to */
    private Folder to;

    /** The folder to copy from */
    private Folder from;

    /** The files to copy */
    private Matchers matchers = new Matchers();

    /** Progress in copying files */
    private final ProgressReporter progress = progressReporter(this, "files");

    public Copier(Build build)
    {
        super(build);
    }

    public Copier(Copier that)
    {
        super(that.associatedBuild());
        this.to = that.to;
        this.from = that.from;
        this.matchers = that.matchers.copy();
    }

    public Copier copy()
    {
        return new Copier(this);
    }

    public Copier withFrom(Folder from)
    {
        var copy = copy();
        copy.from = from;
        return copy;
    }

    public Copier withMatcher(Matcher<ResourcePathed> matcher)
    {
        var copy = copy();
        copy.matchers.add(matcher);
        return copy;
    }

    public Copier withMatcher(String glob)
    {
        var copy = copy();
        copy.matchers.add(glob(glob));
        return copy;
    }

    public Copier withTo(Folder to)
    {
        var copy = copy();
        copy.to = to;
        return copy;
    }

    @Override
    protected String description()
    {
        return format("""
                Copier
                  from: $
                  to: $
                  matchers: $
                """, from, to, matchers);
    }

    @Override
    protected void onRun()
    {
        information("Copying from $ to $",
                from.relativeTo(rootFolder()),
                to.relativeTo(rootFolder()));

        // For each source file in the 'from' folder that matches,
        var files = from.nestedFiles(matchers);
        progress.steps(files.count());
        progress.start("Copying " + files.size() + " files");
        for (var source : files)
        {
            // find the path relative to the root,
            var relative = source.relativeTo(from);

            // construct a file with the same path relative to the 'to' folder,
            var destination = to.file(relative);

            // create any parent folders that might be required
            destination.parent().mkdirs();

            // and copy the source file to the destination location
            source.safeCopyTo(destination, CopyMode.OVERWRITE, progress);
            progress.next();
        }
        progress.end(files.size() + " files copied");
    }
}
