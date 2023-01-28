package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.map.VariableMap;
import com.telenav.kivakit.core.os.OperatingSystem.OperatingSystemType;
import com.telenav.kivakit.core.os.OperatingSystem.ProcessorArchitecture;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;

import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;
import static com.telenav.kivakit.filesystem.Folder.parseFolder;

/**
 * Build environment information, such as operating system and hardware information
 *
 * <p><b>Operating System</b></p>
 *
 * <ul>
 *     <li>{@link #isMac()}</li>
 *     <li>{@link #isUnix()}</li>
 *     <li>{@link #isWindows()}</li>
 *     <li>{@link #operatingSystemType()}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #environmentVariables()}</li>
 *     <li>{@link #property(String)}</li>
 * </ul>
 *
 * <p><b>Hardware</b></p>
 *
 * <ul>
 *     <li>{@link #processorArchitecture()}</li>
 *     <li>{@link #processors()}</li>
 * </ul>
 *
 * <p><b>Tool Home Folders</b></p>
 *
 * <ul>
 *     <li>{@link #javaHome()}</li>
 *     <li>{@link #mavenHome()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface BuildEnvironmentTrait
{
    /**
     * Returns operating system environment variables
     *
     * @return A map of environment variables
     */
    default VariableMap<String> environmentVariables()
    {
        return operatingSystem().environmentVariables();
    }

    /**
     * Returns true if this is running on a Mac
     */
    default boolean isMac()
    {
        return operatingSystem().isMac();
    }

    /**
     * Returns true if this is running on a Unix operating system
     */
    default boolean isUnix()
    {
        return operatingSystem().isUnix();
    }

    /**
     * Returns true if this is running on a Windows operating system
     */
    default boolean isWindows()
    {
        return operatingSystem().isWindows();
    }

    /**
     * Returns the home folder for Java
     */
    default Folder javaHome()
    {
        return parseFolder(operatingSystem().javaHome());
    }

    /**
     * Returns the home folder for Apache Maven
     */
    default Folder mavenHome()
    {
        return parseFolder(property("M2_HOME"));
    }

    /**
     * Returns the {@link OperatingSystemType} this is running on
     */
    default OperatingSystemType operatingSystemType()
    {
        return operatingSystem().operatingSystemType();
    }

    /**
     * Returns the {@link ProcessorArchitecture} this is running on
     */
    default ProcessorArchitecture processorArchitecture()
    {
        return operatingSystem().processorArchitecture();
    }

    /**
     * Returns the number of processors available to this virtual machine
     */
    default Count processors()
    {
        return javaVirtualMachine().processors();
    }

    /**
     * Returns the system property for the given key. If there is no system property defined, returns the environment
     * variable value for the key
     *
     * @param key The property key
     * @return The property value
     */
    default String property(String key)
    {
        return operatingSystem().systemPropertyOrEnvironmentVariable(key);
    }
}
