package digital.fiasco.runtime.build.builder.tools.archiver;

import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FileList;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;

import java.util.Collection;

import static com.telenav.kivakit.filesystem.FileList.fileList;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.AccessMode.WRITE;
import static com.telenav.kivakit.resource.compression.archive.ZipArchive.zipArchive;

/**
 * Archives one or more files to ZIP archive.
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public class Archiver extends BaseTool
{
    /** The files to archive */
    private FileList files;

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
        this.files = that.files.copy();
        this.archiveFile = that.archiveFile;
    }

    /**
     * Returns a copy of this archiver
     */
    public Archiver copy()
    {
        return new Archiver(this);
    }

    /**
     * {@inheritDoc}
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
    public void onRun()
    {
        try (var archive = zipArchive(this, archiveFile, WRITE))
        {
            archive.add(files);
        }
    }

    /**
     * Returns a copy of this archiver with the given files
     *
     * @param files The files to add to the archive
     * @return A copy of this archive with the given files
     */
    public Archiver withAdditionalFiles(Collection<File> files)
    {
        var copy = copy();
        copy.files.addAll(files);
        return copy;
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

    /**
     * Returns a copy of this archiver with the given files
     *
     * @param files The files to add to the archive
     * @return A copy of this archive with the given files
     */
    public Archiver withFiles(Collection<File> files)
    {
        var copy = copy();
        copy.files = fileList(files);
        return copy;
    }
}
