package digital.fiasco.runtime;

import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.filesystem.Folder;

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
    public static Folder fiascoCacheFolder()
    {
        return userHome()
            .folder(".fiasco")
            .mkdirs();
    }
}
