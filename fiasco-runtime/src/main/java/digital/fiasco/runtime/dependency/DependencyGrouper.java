package digital.fiasco.runtime.dependency;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;

/**
 * Plans for parallel processing of a dependency tree by repeatedly breaking the tree into groups of dependencies which
 * have no yet-ungrouped dependencies.
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
        DependencyList<T> dependencies = tree.depthFirst();

        // then while we haven't grouped all dependencies,
        while (grouped.size() < dependencies.size())
        {
            var group = new DependencyList<T>();

            // go through all dependencies,
            for (T dependency : dependencies)
            {
                // and if all of our dependency's dependencies are already grouped,
                if (grouped.containsAll(dependency.dependencies().asSet()))
                {
                    // add the dependency to this group,
                    group.add(dependency);

                    // and mark the dependency as having been grouped.
                    grouped.add(dependency);
                }
            }

            // add the group we just built to the list of groups
            groups.add(group);
        }

        return groups;
    }
}
