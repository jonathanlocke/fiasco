package digital.fiasco.runtime.build.builder.phases.standard;

import com.telenav.kivakit.component.ComponentMixin;
import com.telenav.kivakit.filesystem.Folder;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.phases.Phase;
import digital.fiasco.runtime.build.builder.phases.PhaseList;
import digital.fiasco.runtime.build.builder.tools.ToolFactory;

import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;

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

        add(new PhaseStart());
        add(new PhaseClean().duringPhase(it -> onClean()));
        add(new PhasePrepare().duringPhase(it -> onPrepare()));
        add(new PhaseCompile().duringPhase(it -> onCompile()));
        add(new PhaseTest());
        add(new PhaseDocument());
        add(new PhasePackage());
        add(new PhaseIntegrationTest());
        add(new PhaseInstall());
        add(new PhaseDeployPackages());
        add(new PhaseDeployDocumentation());
        add(new PhaseEnd());

        enable(phase("start"));
        enable(phase("end"));
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
            .withSourceFolder(sourceMainResourcesFolder())
            .withTargetFolder(targetClassesFolder())
            .with("**/*")
            .run();

        newCopier()
            .withSourceFolder(sourceTestResourcesFolder())
            .withTargetFolder(targetTestClassesFolder())
            .with("**/*")
            .run();
    }

    @Override
    public Folder rootFolder()
    {
        return builder.rootFolder();
    }
}
