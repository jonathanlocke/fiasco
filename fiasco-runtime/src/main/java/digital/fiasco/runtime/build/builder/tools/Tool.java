//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.BuildEnvironmentTrait;
import digital.fiasco.runtime.build.settings.BuildProfile;
import digital.fiasco.runtime.build.builder.BuildStructured;
import digital.fiasco.runtime.build.builder.BuilderAssociated;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;

/**
 * Interface for executable tools. Tools are runnable message repeaters that are associated with a {@link BaseBuild}
 * that can be retrieved with build methods.
 *
 * @author Jonathan locke
 */
public interface Tool extends
    Runnable,
    Repeater,
    Described,
    BuilderAssociated,
    BuildStructured,
    BuildEnvironmentTrait
{
    /**
     * Returns the list of dependencies from the builder associated with this tool
     *
     * @return The dependency list
     */
    DependencyList<?, ?> dependencies();

    /**
     * Enables this tool for the given profile
     *
     * @param profile The profile
     * @return This tool for method chaining
     */
    BaseTool enableForProfile(BuildProfile profile);

    /**
     * Returns true if this tool is enabled under any of the profiles it is assigned to
     *
     * @return True if enabled
     */
    boolean isEnabled();

    /**
     * Returns the librarian used by the builder associated with this tool
     *
     * @return The librarian
     */
    Librarian librarian();

    /**
     * Called to describe what this tool does (without doing it)
     */
    void onDescribe();

    /**
     * Called after this tool has been run
     */
    void onRan();

    /**
     * Called when this tool runs
     */
    void onRun();

    /**
     * Called before this tool runs
     */
    void onRunning();
}
