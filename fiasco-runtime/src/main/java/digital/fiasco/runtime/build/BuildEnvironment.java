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
 * Operating system and hardware information
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface BuildEnvironment
{
    default VariableMap<String> environmentVariables()
    {
        return operatingSystem().environmentVariables();
    }

    default boolean isMac()
    {
        return operatingSystem().isMac();
    }

    default boolean isUnix()
    {
        return operatingSystem().isUnix();
    }

    default boolean isWindows()
    {
        return operatingSystem().isWindows();
    }

    default Folder javaHome()
    {
        return parseFolder(operatingSystem().javaHome());
    }

    default Folder mavenHome()
    {
        return parseFolder(property("M2_HOME"));
    }

    default OperatingSystemType operatingSystemType()
    {
        return operatingSystem().operatingSystemType();
    }

    default ProcessorArchitecture processorArchitecture()
    {
        return operatingSystem().processorArchitecture();
    }

    default Count processors()
    {
        return javaVirtualMachine().processors();
    }

    default String property(String key)
    {
        return operatingSystem().systemPropertyOrEnvironmentVariable(key);
    }
}
