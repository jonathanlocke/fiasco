package digital.fiasco.runtime.dependency.processing;

import com.telenav.kivakit.core.collections.list.ObjectList;

import java.util.Collection;

public class TaskList<T> extends ObjectList<Task<T>>
{
    public TaskList()
    {
    }

    public TaskList(Collection<Task<T>> tasks)
    {
        super(tasks);
    }
}
