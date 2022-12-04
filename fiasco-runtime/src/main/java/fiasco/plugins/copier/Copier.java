//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package fiasco.plugins.copier;

import com.telenav.kivakit.core.progress.ProgressReporter;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.comparison.Matcher;
import com.telenav.kivakit.resource.CopyMode;
import com.telenav.kivakit.resource.ResourcePathed;
import fiasco.Module;
import fiasco.plugins.Plugin;

import static com.telenav.kivakit.core.progress.reporters.BroadcastingProgressReporter.progressReporter;

/**
 * Copies selected files from one folder to another.
 *
 * @author shibo
 */
@SuppressWarnings("unused")
public class Copier extends Plugin
{
    /** The folder to copy to */
    private Folder to;

    /** The folder to copy from */
    private Folder from;

    /** The files to copy */
    private Matcher<ResourcePathed> matcher = Matcher.matchAll();

    /** Progress in copying files */
    private final ProgressReporter progress = progressReporter(this, "files");

    public Copier(Module module)
    {
        super(module);
    }

    public Copier from(Folder from)
    {
        this.from = resolveFolder(from);
        return this;
    }

    public Folder from()
    {
        return from;
    }

    public Matcher<ResourcePathed> matching()
    {
        return matcher;
    }

    public Copier matching(Matcher<ResourcePathed> matcher)
    {
        this.matcher = matcher;
        return this;
    }

    public Copier to(Folder to)
    {
        this.to = resolveFolder(to);
        return this;
    }

    public Folder to()
    {
        return to;
    }

    @Override
    protected void onRun()
    {
        // For each source file in the from folder that matches,
        var files = from.nestedFiles(matcher);
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
