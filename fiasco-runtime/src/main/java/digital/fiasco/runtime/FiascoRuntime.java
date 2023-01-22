package digital.fiasco.runtime;

import com.telenav.kivakit.annotations.code.quality.MethodQuality;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.filesystem.Folder;

import static com.telenav.kivakit.annotations.code.quality.Documentation.DOCUMENTED;
import static com.telenav.kivakit.annotations.code.quality.Testing.TESTED;
import static com.telenav.kivakit.filesystem.Folders.userHome;

/**
 * The {@link Project} for Fiasco.
 *
 * @author Jonathan locke
 */
public class FiascoRuntime extends Project
{
    /**
     * Returns the Fiasco cache folder in ~/.fiasco
     *
     * @return The cache folder
     */
    @MethodQuality(documentation = DOCUMENTED, testing = TESTED)
    public static Folder fiascoCacheFolder()
    {
        return userHome()
            .folder(".fiasco")
            .mkdirs();
    }
}
