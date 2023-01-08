package digital.fiasco.runtime.dependency.processing;

import com.telenav.kivakit.interfaces.naming.Named;

import java.util.concurrent.Callable;

/**
 * A {@link Named} {@link Callable} returning a {@link TaskResult}
 *
 * @author Jonathan Locke
 */
public interface Task extends Callable<TaskResult>, Named
{
}
