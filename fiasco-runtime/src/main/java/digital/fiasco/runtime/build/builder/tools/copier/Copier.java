//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.builder.tools.copier;

import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.filesystem.Folder.Traversal;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.ResourcePathed;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;

import static com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter.progressReporter;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.resource.ResourceGlob.glob;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;

/**
 * Copies selected files from one folder to another.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Copier extends BaseTool
{
    /** The folder to copy to */
    private Folder to;

    /** The folder to copy from */
    private Folder source;

    /** Pattern matching files to include */
    private Matcher<ResourcePathed> includes;

    /** Pattern matching files to include */
    private Matcher<ResourcePathed> excludes;

    /** Progress in copying files */
    private final ProgressReporter progress = progressReporter(this, "files");

    /** The type of traversal to use when copying */
    private Traversal traversal;

    /**
     * Creates a copier associated with the given builder
     *
     * @param builder The builder
     */
    public Copier(Builder builder)
    {
        super(builder);
    }

    /**
     * Creates a copy of the given copier
     *
     * @param that The copier to copy
     */
    public Copier(Copier that)
    {
        super(that.associatedBuilder());
        this.to = that.to;
        this.source = that.source;
        this.includes = that.includes;
        this.excludes = that.excludes;
        this.traversal = that.traversal;
    }

    /**
     * Returns a copy of this copier
     */
    public Copier copy()
    {
        return new Copier(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return format("""
            Copier
              from: $
              to: $
              includes: $
              excludes: $
            """, source, to, includes, excludes);
    }

    /**
     * Returns a copy of this copier that includes resources matching the given glob
     *
     * @param glob The glob pattern
     * @return The new copier
     */
    public Copier with(String glob)
    {
        return with(glob(glob));
    }

    /**
     * Returns a copy of this copier that includes resources matching the given matcher
     *
     * @param matcher The matcher
     * @return The new copier
     */
    public Copier with(Matcher<ResourcePathed> matcher)
    {
        var copy = copy();
        copy.includes = (Matcher<ResourcePathed>) includes.and(matcher);
        return copy;
    }

    /**
     * Returns a copy of this copier with the given source folder
     *
     * @param source The source folder
     * @return The new copier
     */
    public Copier withSourceFolder(Folder source)
    {
        var copy = copy();
        copy.source = source;
        return copy;
    }

    /**
     * Returns a copy of this copier with the given target folder
     *
     * @param target The target folder
     * @return The new copier
     */
    public Copier withTargetFolder(Folder target)
    {
        var copy = copy();
        copy.to = target;
        return copy;
    }

    /**
     * Returns a copy of this copier with the given traversal style
     *
     * @param traversal The traversal to use when copying recursively
     * @return The new copier
     */
    public Copier withTraversal(Traversal traversal)
    {
        var copy = copy();
        copy.traversal = traversal;
        return copy;
    }

    /**
     * Returns a copy of this copier that excludes the resources matching the given glob pattern
     *
     * @param glob The glob pattern
     * @return The new copier
     */
    public Copier without(String glob)
    {
        var copy = copy();
        copy.excludes = glob(glob);
        return copy;
    }

    /**
     * Returns a copy of this copier that excludes the resources matching the given matcher
     *
     * @param matcher The matcher
     * @return The new copier
     */
    public Copier without(Matcher<ResourcePathed> matcher)
    {
        var copy = copy();
        copy.excludes = (Matcher<ResourcePathed>) excludes.or(matcher);
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRun()
    {
        information("Copying from $ to $",
            source.relativeTo(rootFolder()),
            to.relativeTo(rootFolder()));

        // For each source file in the 'from' folder that matches,
        var files = source.files(file -> (includes == null || includes.matches(file))
            && (excludes == null || !excludes.matches(file)), traversal);
        progress.steps(files.count());
        progress.start("Copying " + files.size() + " files");
        for (var source : files)
        {
            // find the path relative to the root,
            var relative = source.relativeTo(this.source);

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
