//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.tools.copier;

import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Folder.Traversal;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

import static com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter.progressReporter;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.resource.CopyMode.OVERWRITE;
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

    /** Pattern matching files to include */
    private Matcher<ResourcePathed> includes;

    /** Pattern matching files to include */
    private Matcher<ResourcePathed> excludes;

    /** Progress in copying files */
    private final ProgressReporter progress = progressReporter(this, "files");

    private Traversal traversal;

    public Copier(Build build)
    {
        super(build);
    }

    public Copier(Copier that)
    {
        super(that.associatedBuild());
        this.to = that.to;
        this.from = that.from;
        this.includes = that.includes;
        this.excludes = that.excludes;
        this.traversal = that.traversal;
    }

    public Copier copy()
    {
        return new Copier(this);
    }

    public Copier excluding(String glob)
    {
        var copy = copy();
        copy.excludes = glob(glob);
        return copy;
    }

    public Copier excluding(Matcher<ResourcePathed> matcher)
    {
        var copy = copy();
        copy.excludes = (Matcher<ResourcePathed>) excludes.or(matcher);
        return copy;
    }

    public Copier including(Matcher<ResourcePathed> matcher)
    {
        var copy = copy();
        copy.includes = (Matcher<ResourcePathed>) includes.and(matcher);
        return copy;
    }

    public Copier including(String glob)
    {
        return including(glob(glob));
    }

    public Copier withFrom(Folder from)
    {
        var copy = copy();
        copy.from = from;
        return copy;
    }

    public Copier withTo(Folder to)
    {
        var copy = copy();
        copy.to = to;
        return copy;
    }

    public Copier withTraversal(Traversal traversal)
    {
        var copy = copy();
        copy.traversal = traversal;
        return copy;
    }

    @Override
    protected String description()
    {
        return format("""
                Copier
                  from: $
                  to: $
                  includes: $
                  excludes: $
                """, from, to, includes, excludes);
    }

    @Override
    protected void onRun()
    {
        information("Copying from $ to $",
                from.relativeTo(rootFolder()),
                to.relativeTo(rootFolder()));

        // For each source file in the 'from' folder that matches,
        var files = from.files(file -> (includes == null || includes.matches(file))
                && (excludes == null || !excludes.matches(file)), traversal);
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
            source.safeCopyTo(destination, OVERWRITE, progress);
            progress.next();
        }
        progress.end(files.size() + " files copied");
    }
}
