//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.interfaces.object.Copyable;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.BaseBuild;
import digital.fiasco.runtime.build.builder.BuilderAssociated;
import digital.fiasco.runtime.build.environment.BuildEnvironmentTrait;
import digital.fiasco.runtime.build.environment.BuildStructure;
import digital.fiasco.runtime.dependency.collections.lists.ArtifactList;
import digital.fiasco.runtime.librarian.Librarian;

/**
 * Interface for executable tools. Tools are runnable message repeaters that are associated with a {@link BaseBuild}
 * that can be retrieved with build methods.
 *
 * @author Jonathan locke
 */
public interface Tool<T extends Tool<T, O>, O> extends
    Copyable<T>,
    Repeater,
    Described,
    BuilderAssociated,
    BuildStructure,
    BuildEnvironmentTrait
{
    /**
     * Returns the list of artifact dependencies from the builder associated with this tool
     *
     * @return The dependency list
     */
    ArtifactList artifactDependencies();

    /**
     * Throws an exception if this tool has an inconsistent configuration
     *
     * @throws RuntimeException Thrown if the tool can't be run because its settings are invalid
     */
    void checkConsistency();

    /**
     * Returns true if this tool is enabled under any of the profiles to which it is assigned
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
     * Called after this tool has been run
     */
    void onRan();

    /**
     * Called when this tool runs
     */
    O onRun();

    /**
     * Called before this tool runs
     */
    void onRunning();

    /**
     * Runs this tool and returns its output
     *
     * @return The output from the tool, if any
     */
    O run();
}
