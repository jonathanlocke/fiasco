package digital.fiasco.runtime.build.builder.phases.standard;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.tools.ToolFactory;

import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_CLEAN;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_COMPILE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_DEPLOY_DOCUMENTATION;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_DEPLOY_PACKAGES;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_DOCUMENT;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_END;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_INSTALL;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_INTEGRATION_TEST;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_PACKAGE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_PREPARE;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_START;
import static digital.fiasco.runtime.build.builder.phases.Phase.PHASE_TEST;

/**
 * Defines a standard list of phases, each with a default implementation:
 *
 * <ol>
 *     <li>start - start of phases</li>
 *     <li>clean - removes target files</li>
 *     <li>prepare - prepares sources and resources for compilation</li>
 *     <li>compile - compiles sources</li>
 *     <li>test - runs unit tests</li>
 *     <li>document - creates documentation</li>
 *     <li>package - assembles targets into packages</li>
 *     <li>integration-test - runs integration tests</li>
 *     <li>install - installs packages in local repository</li>
 *     <li>deploy-packages - deploys packages to remote repositories</li>
 *     <li>deploy-documentation - deploys documentation</li>
 *     <li>end - end of phases</li>
 * </ol>
 *
 * <p>
 * The default implementation for each phase can be found in the method onX(), where X is the phase name.
 * To add new phases to this default list, use {@link #addPhaseBefore(String, Phase)} and {@link #addPhaseAfter(String, Phase)}.
 * To replace a phase with a new definition, use {@link #replacePhase(String, Phase)}.
 * </p>
 *
 * @author Jonathan Locke
 */
public class StandardPhases extends PhaseList implements
    ToolFactory,
    ComponentMixin
{
    /** The builder for this list of phases */
    private final Builder builder;

    /**
     * Installs the default phases
     */
    public StandardPhases(Builder builder)
    {
        this.builder = builder;

        add(PHASE_START);
        add(PHASE_CLEAN.duringPhase(it -> onClean()));
        add(PHASE_PREPARE.duringPhase(it -> onPrepare()));
        add(PHASE_COMPILE.duringPhase(it -> onCompile()));
        add(PHASE_TEST);
        add(PHASE_DOCUMENT);
        add(PHASE_PACKAGE);
        add(PHASE_INTEGRATION_TEST);
        add(PHASE_INSTALL);
        add(PHASE_DEPLOY_PACKAGES);
        add(PHASE_DEPLOY_DOCUMENTATION);
        add(PHASE_END);

        enable(PHASE_START);
        enable(PHASE_END);
    }

    @Override
    public Builder associatedBuilder()
    {
        return builder;
    }

    public void onClean()
    {
        newCleaner()
            .withFiles(targetFolder()
                .nestedFiles(matchAll()))
            .run();
    }

    public void onCompile()
    {
        newCompiler()
            .withSources(sourceMainJavaSources())
            .run();

        newStamper().run();
    }

    public void onPrepare()
    {
        newCopier()
            .withTargetFolder(targetClassesFolder())
            .withFiles(sourceMainResourcesFolder().nestedFiles())
            .run();

        newCopier()
            .withTargetFolder(targetTestClassesFolder())
            .withFiles(sourceTestResourcesFolder().nestedFiles())
            .run();
    }

    @Override
    public Folder rootFolder()
    {
        return builder.rootFolder();
    }
}
