package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;

public class DependencyBatcher
{
    /**
     * Breaks the given dependency tree into batches that can be executed in parallel
     *
     * @param tree The tree to analyze
     * @return A list of dependency lists, each of which can be resolved in parallel (once prior lists have been
     * resolved)
     */
    public ObjectList<DependencyList> batches(DependencyTree tree)
    {
        return null;
    }
}
