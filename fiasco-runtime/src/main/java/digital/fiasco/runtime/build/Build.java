//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////////////////////////////
// © 2020 Telenav  - All rights reserved.                                                                    /
// This software is the confidential and proprietary information of Telenav ("Confidential Information").    /
// You shall not disclose such Confidential Information and shall use it only in accordance with the         /
// terms of the license agreement you entered into with Telenav.                                             /
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

package digital.fiasco.runtime.build;

import com.telenav.kivakit.application.Application;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;
import digital.fiasco.runtime.build.environment.EnvironmentTrait;
import digital.fiasco.runtime.build.metadata.Metadata;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseBuildEnd;
import digital.fiasco.runtime.build.phases.PhaseBuildStart;
import digital.fiasco.runtime.build.phases.PhaseClean;
import digital.fiasco.runtime.build.phases.PhaseCompile;
import digital.fiasco.runtime.build.phases.PhaseDeployDocumentation;
import digital.fiasco.runtime.build.phases.PhaseDeployPackages;
import digital.fiasco.runtime.build.phases.PhaseDocument;
import digital.fiasco.runtime.build.phases.PhaseInstall;
import digital.fiasco.runtime.build.phases.PhaseIntegrationTest;
import digital.fiasco.runtime.build.phases.PhasePackage;
import digital.fiasco.runtime.build.phases.PhasePrepare;
import digital.fiasco.runtime.build.phases.PhaseTest;
import digital.fiasco.runtime.build.structure.BuildStructureMixin;
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.build.tools.librarian.Library;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.artifact.Artifact;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.ensure.Ensure.illegalState;
import static digital.fiasco.runtime.repository.artifact.Artifact.parseArtifact;

@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused" })
public abstract class Build extends Application implements
        ToolFactory,
        EnvironmentTrait,
        BuildStructureMixin,
        BuildAttached,
        BuildListener
{
    /** The primary artifact being built */
    private Artifact artifact;

    /** Metadata about the build */
    private Metadata metadata;

    /** Listeners to call as the build proceeds */
    private final ObjectList<BuildListener> buildListeners = list(this);

    private final DependencyList<Library> libraries = new DependencyList<>();

    /** Maps from the name of a phase to the {@link Phase} object */
    private final ObjectMap<String, Phase> nameToPhase = new ObjectMap<>();

    /** Phases in order of execution */
    private final ObjectList<Phase> orderedPhases = list();

    /** Enable state of each phase */
    private final ObjectMap<Phase, Boolean> phaseEnabled = new ObjectMap<>();

    protected Build()
    {
        // Do not allow subclass constructors
        for (Class<?> at = getClass(); at != Build.class; at = at.getSuperclass())
        {
            if (at.getConstructors().length > 0)
            {
                illegalState("BuildBuild subclasses cannot declare constructors");
            }
        }

        // Install default build phases
        onInstallPhases();
    }

    /**
     * Adds the given build listener to this build
     *
     * @param listener The listener to call with build events
     */
    public void addListener(BuildListener listener)
    {
        buildListeners.add(listener);
    }

    /**
     * Appends the given phase to the end of the list of phases
     *
     * @param phase The phase to add
     */
    public void addPhase(Phase phase)
    {
        nameToPhase.put(phase.name(), phase);
        orderedPhases.add(phase);
    }

    /**
     * Inserts the given phase after the named phase
     *
     * @param name The name of the phase to insert after
     * @param phase The phase to insert
     */
    public void addPhaseAfter(String name, Phase phase)
    {
        var at = orderedPhases.indexOf(phase(name));
        orderedPhases.add(at + 1, phase);
        nameToPhase.put(phase.name(), phase);
    }

    /**
     * Inserts the given phase before the named phase
     *
     * @param name The name of the phase to insert before
     * @param phase The phase to insert
     */
    public void addPhaseBefore(String name, Phase phase)
    {
        var at = orderedPhases.indexOf(phase(name));
        orderedPhases.add(at, phase);
        nameToPhase.put(phase.name(), phase);
    }

    public Artifact artifact()
    {
        return artifact;
    }

    public ObjectList<BuildListener> buildListeners()
    {
        return buildListeners;
    }

    @Override
    public Build attachedToBuild()
    {
        return this;
    }

    /**
     * Disables the given phase from execution during a build
     *
     * @param phase The phase to disable
     */
    public void disable(Phase phase)
    {
        for (var at : phase.requiredPhases())
        {
            phaseEnabled.put(at, false);
        }
        phaseEnabled.put(phase, false);
    }

    /**
     * Enables the given phase for execution during a build
     *
     * @param phase The phase to enable
     */
    public void enable(Phase phase)
    {
        for (var at : phase.requiredPhases())
        {
            phaseEnabled.put(at, true);
        }
        phaseEnabled.put(phase, true);
    }

    /**
     * Builds with one thread for each processor
     *
     * @return True if the build succeeded without any problems
     */
    public final boolean execute()
    {
        return execute(processors());
    }

    /**
     * Builds with the given number of worker threads
     *
     * @return True if the build succeeded without any problems
     */
    @SuppressWarnings("unchecked")
    public final boolean execute(Count threads)
    {
        // Listen to any problems broadcast by the build,
        var issues = new MessageList(message -> !message.status().succeeded());
        issues.listenTo(this);

        // go through each phase in order,
        for (var phase : orderedPhases)
        {
            // and if the phase is enabled,
            if (enabled(phase))
            {
                // notify that the phase has started,
                var multicaster = new BuildMulticaster(buildListeners);
                multicaster.onPhaseStart(phase);

                // run the phase calling all listeners,
                phase.run(multicaster);

                // notify that the phase has ended,
                multicaster.onPhaseEnd(phase);
            }
        }

        // then collect statistics and display them,
        var statistics = issues.statistics(Problem.class, Warning.class, Quibble.class);
        information(statistics.titledBox("Build Results"));

        // and return true if there are no problems (or worse).
        return issues.countWorseThanOrEqualTo(Problem.class).isZero();
    }

    public DependencyList<Library> libraries()
    {
        return libraries;
    }

    public void metadata(Metadata metadata)
    {
        this.metadata = metadata;
    }

    public Metadata metadata()
    {
        return metadata;
    }

    public Phase phase(String name)
    {
        return ensureNotNull(nameToPhase.get(name), "Could not find phase: $", name);
    }

    public ToolFactory requires(Library library)
    {
        libraries.add(library);
        return this;
    }

    protected void artifact(String descriptor)
    {
        artifact(parseArtifact(descriptor));
    }

    protected void artifact(Artifact artifact)
    {
        this.artifact = artifact;
    }

    protected final void onInstallPhases()
    {
        addPhase(new PhaseBuildStart());
        addPhase(new PhaseClean());
        addPhase(new PhasePrepare());
        addPhase(new PhaseCompile());
        addPhase(new PhaseTest());
        addPhase(new PhaseDocument());
        addPhase(new PhasePackage());
        addPhase(new PhaseIntegrationTest());
        addPhase(new PhaseInstall());
        addPhase(new PhaseDeployPackages());
        addPhase(new PhaseDeployDocumentation());
        addPhase(new PhaseBuildEnd());
    }

    @Override
    protected void onRun()
    {
        for (var argument : argumentList())
        {
            var value = argument.value();
            if (value.startsWith("-"))
            {
                disable(phase(value));
            }
            else
            {
                enable(phase(value));
            }
        }
    }

    private boolean enabled(Phase phase)
    {
        return phaseEnabled.get(phase);
    }
}
