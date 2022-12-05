package fiasco.environment;

import com.telenav.kivakit.core.value.count.Count;

import static com.telenav.kivakit.core.os.OperatingSystem.operatingSystem;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;

/**
 * Operating system and hardware information
 *
 * @author jonathanl (shibo)
 */
@SuppressWarnings("unused")
public interface EnvironmentTrait
{
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

    default Count processors()
    {
        return javaVirtualMachine().processors();
    }
}
