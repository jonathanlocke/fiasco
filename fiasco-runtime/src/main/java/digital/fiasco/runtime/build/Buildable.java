package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.value.count.Count;
import digital.fiasco.runtime.build.tools.builder.Builder;
import digital.fiasco.runtime.build.tools.librarian.Librarian;

@SuppressWarnings({ "UnusedReturnValue", "unused" })
public interface Buildable extends BuildEnvironment
{
    /**
     * Returns the build listeners for this build
     */
    ObjectList<BuildListener> buildListeners();

    /**
     * Returns the {@link Builder} for this build
     */
    Builder builder();

    /**
     * Returns the child build at the given path
     *
     * @param path The child path
     * @return The child build
     */
    Build childBuild(String path);

    /**
     * Disables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    Build disable(BuildOption option);

    /**
     * Enables the given build option
     *
     * @param option The option
     * @return This build for method chaining
     */
    Build enable(BuildOption option);

    /**
     * Returns true if the given option is enabled
     *
     * @param option The option
     * @return True if the option is enabled
     */
    boolean isEnabled(BuildOption option);

    /**
     * Returns the librarian for this build
     *
     * @return The librarian
     */
    default Librarian librarian()
    {
        return builder().librarian();
    }

    /**
     * Builds with one thread for each processor
     *
     * @return True if the build succeeded without any problems
     */
    default boolean runBuild()
    {
        return builder().runBuild();
    }

    /**
     * Builds with the given number of worker threads
     *
     * @return True if the build succeeded without any problems
     */
    default boolean runBuild(Count threads)
    {
        return builder().runBuild(threads);
    }
}
