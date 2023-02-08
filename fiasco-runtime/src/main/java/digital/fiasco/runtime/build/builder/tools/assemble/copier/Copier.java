//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.builder.tools.assemble.copier;

import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseFileTool;

import static com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter.progressReporter;
import static com.telenav.kivakit.core.string.Formatter.format;
import static com.telenav.kivakit.resource.WriteMode.OVERWRITE;

/**
 * Copies selected files from one folder to another.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class Copier extends BaseFileTool<Copier, Void>
{
    /** The folder to copy to */
    private Folder to;

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
    }

    /**
     * Returns a copy of this copier
     */
    @Override
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
              to: $
              files: $
            """, to, files());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Void onRun()
    {
        if (files().isNonEmpty())
        {
            var sourceFolder = files().parent();
            information("Copying $ files from $ to $", files().count(), sourceFolder, to.relativeTo(sourceFolder));

            // For each source file in the 'from' folder that matches,
            var files = files();
            var progress = progressReporter(listener(), "files");
            progress.steps(files.count());
            progress.start("Copying " + files.size() + " files");
            for (var source : files)
            {
                // find the path relative to the root,
                var relative = source.relativeTo(sourceFolder);

                // construct a file with the same path relative to the 'to' folder,
                var destination = to.file(relative);

                // create any parent folders that might be required
                destination.parent().mkdirs();

                // and copy the source file to the destination location
                step(() -> source.safeCopyTo(destination, OVERWRITE, progress), "Copying $ to $", source, destination);
                progress.next();
            }
            progress.end(files.size() + " files copied");
        }

        return null;
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
}
