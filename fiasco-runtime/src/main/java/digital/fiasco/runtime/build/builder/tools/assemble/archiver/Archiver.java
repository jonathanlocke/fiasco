package digital.fiasco.runtime.build.builder.tools.assemble.archiver;

import com.telenav.kivakit.filesystem.File;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseFileTool;

import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.WRITE;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;

/**
 * Archives one or more files to ZIP archive.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Archiver extends BaseFileTool<Archiver, Void>
{
    /** The archive */
    private File archiveFile;

    /**
     * Creates an archiver associated with the given builder
     *
     * @param builder The builder
     */
    public Archiver(Builder builder)
    {
        super(builder);
    }

    /**
     * Creates a copy of the given archiver
     *
     * @param that The archiver to copy
     */
    public Archiver(Archiver that)
    {
        super(that.associatedBuilder());
        this.archiveFile = that.archiveFile;
    }

    /**
     * Returns a copy of this archiver
     */
    @Override
    public Archiver copy()
    {
        return new Archiver(this);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public String description()
    {
        return "Archives resources";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Void onRun()
    {
        if (shouldDescribe())
        {
            step("Archiving to $:\n$", archiveFile, files()
                .asStringList()
                .indented(4)
                .join("\n"));
        }
        else
        {
            try (var archive = zipArchive(this, archiveFile, WRITE))
            {
                archive.add(files());
            }
        }

        return null;
    }

    /**
     * Returns a copy of this archiver with the given archive file
     *
     * @param archive The archive file
     * @return A copy of this archive with the given file
     */
    public Archiver withArchive(File archive)
    {
        var copy = copy();
        copy.archiveFile = archive;
        return copy;
    }
}
