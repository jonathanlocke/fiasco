package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;

/**
 * Plans for parallel processing of a dependency tree by repeatedly breaking the tree into groups of dependencies which
 * have no yet-ungrouped dependencies.
 *
 * <pre>
 *      a       <--- Group 2
 *     / \
 *    b   d     <--- Group 1
 *   /   / \
 *  c   e   f   <--- Group 0</pre>
 *
 * @author Jonathan Locke
 */
public class DependencyGrouper<T extends Dependency>
{
    /**
     * Breaks the given dependency tree into batches that can be executed in parallel
     *
     * @param tree The tree to analyze
     * @return A list of dependency lists, each of which can be resolved in parallel (once prior lists have been
     * resolved)
     */
    public ObjectList<DependencyList<T>> group(DependencyTree<T> tree, Class<T> type)
    {
        var grouped = new ObjectSet<Dependency>();
        var groups = new ObjectList<DependencyList<T>>();

        // Get a depth first traversal of the dependency tree,
        var dependencies = tree.depthFirst();

        // then while we haven't grouped all dependencies,
        while (grouped.size() < dependencies.size())
        {
            var group = new DependencyList<T>();

            // go through all ungrouped dependencies,
            for (var dependency : dependencies.matching(at -> !grouped.contains(at)))
            {
                // and if the dependency has no (ungrouped) dependencies,
                if (hasNoUngroupedDependencies(grouped, dependency))
                {
                    // then it can be added to this group.
                    group = group.with(dependency);
                }
            }

            // Add the group to the set of grouped dependencies,
            group.forEach(grouped::add);

            // then add the group we just built to the list of groups
            groups.add(group);
        }

        return groups;
    }

    private boolean hasNoUngroupedDependencies(ObjectSet<Dependency> grouped, T dependency)
    {
        for (var at : dependency.dependencies())
        {
            if (!grouped.contains(at))
            {
                return false;
            }
        }
        return true;
    }
}
