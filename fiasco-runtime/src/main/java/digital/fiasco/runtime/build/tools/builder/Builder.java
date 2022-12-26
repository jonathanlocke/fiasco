package digital.fiasco.runtime.build.tools.builder;

import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.File;
import com.telenav.kivakit.filesystem.FileList;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildPhased;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseClean;
import digital.fiasco.runtime.build.phases.PhaseCompile;
import digital.fiasco.runtime.build.phases.PhaseDeployDocumentation;
import digital.fiasco.runtime.build.phases.PhaseDeployPackages;
import digital.fiasco.runtime.build.phases.PhaseDocument;
import digital.fiasco.runtime.build.phases.PhaseEnd;
import digital.fiasco.runtime.build.phases.PhaseInstall;
import digital.fiasco.runtime.build.phases.PhaseIntegrationTest;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.phases.PhasePackage;
import digital.fiasco.runtime.build.phases.PhasePrepare;
import digital.fiasco.runtime.build.phases.PhaseStart;
import digital.fiasco.runtime.build.phases.PhaseTest;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.build.tools.librarian.Librarian;

import java.util.Collection;
import java.util.List;

import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static com.telenav.kivakit.filesystem.FileList.fileList;
import static digital.fiasco.runtime.build.BuildOption.DRY_RUN;

/**
 * Builds a project in phases
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * Build phases are executed in a pre-defined order, as below. Each phase is defined by a class that extends
 * {@link Phase}. Additional phases can be inserted into {@link #phases()} with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}.
 * </p>
 *
 * <ul>
 *     <li>{@link #disable(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #enabled(Phase)}</li>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #phases()}</li>
 * </ul>
 *
 * <p>
 * Phases are enabled and disabled with {@link #enable(Phase)} and {@link #disable(Phase)}. The {@link #onRun()} method
 * of the application enables and disables any phase names that were passed from the command line. The phase name itself
 * enables the phase and any dependent phases (for example, "compile" enables the "build-start", "prepare" and "compile"
 * phases). If the phase name is preceded by a dash (for example, -test), the phase is disabled (but not its dependent
 * phases).
 * </p>
 *
 * <ol>
 *     <li>start</li>
 *     <li>clean</li>
 *     <li>prepare</li>
 *     <li>compile</li>
 *     <li>test</li>
 *     <li>document</li>
 *     <li>package</li>
 *     <li>integration-test</li>
 *     <li>install</li>
 *     <li>deploy-packages</li>
 *     <li>deploy-documentation</li>
 *     <li>end</li>
 * </ol>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "StringConcatenationInLoop", "UnusedReturnValue" })
public class Builder extends BaseTool implements
    ToolFactory,
    BuildPhased
{
    /** Phases in order of execution */
    private final PhaseList phases = new PhaseList();

    /** Listeners to call as the build proceeds */
    private ObjectList<BuildListener> buildListeners = list();

    /** Enable state of each phase */
    private final ObjectMap<Phase, Boolean> phaseEnabled = new ObjectMap<>();

    /** The librarian to manage libraries */
    private final Librarian librarian = listenTo(newLibrarian());

    private FileList files = fileList();

    public Builder(Build build)
    {
        super(build);
        installDefaultPhases();
    }

    /**
     * Adds the given build listener to this build
     *
     * @param listener The listener to call with build events
     */
    public void addBuildListener(BuildListener listener)
    {
        buildListeners.add(listener);
    }

    public ObjectList<BuildListener> buildListeners()
    {
        return buildListeners;
    }

    @Override
    public Builder builder()
    {
        return this;
    }

    public Builder copy()
    {
        var copy = new Builder(associatedBuild());
        copy.files = files.copy();
        this.buildListeners = buildListeners.copy();
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        var description = """
            Commands
                            
              command               description
              -----------           ---------------------------------------------
              describe              describe the build rather than running it
                            
            Phases (those preceded by a dash will be disabled)
                        
              phase                 description
              -----------           ---------------------------------------------
            """;

        for (var phase : phases)
        {
            description += String.format("  %-22s%s\n", phase.name(), phase.description());
        }

        return description;
    }

    /**
     * Disables the given phase from execution during a build
     *
     * @param phase The phase to disable
     */
    @Override
    public void disable(Phase phase)
    {
        phaseEnabled.put(phase, false);
    }

    /**
     * Enables the given phase for execution during a build
     *
     * @param phase The phase to enable
     */
    @Override
    public void enable(Phase phase)
    {
        for (var at : phase.requiredPhases())
        {
            phaseEnabled.put(at, true);
        }
        phaseEnabled.put(phase, true);
    }

    @Override
    public boolean isEnabled(Phase phase)
    {
        return phaseEnabled.get(phase);
    }

    /**
     * Returns the librarian for this build
     *
     * @return The librarian
     */
    @Override
    public Librarian librarian()
    {
        return librarian;
    }

    /**
     * Returns the list of phases in execution order for this build
     */
    @Override
    public PhaseList phases()
    {
        return phases;
    }

    /**
     * Builds with one thread for each processor
     *
     * @return True if the build succeeded without any problems
     */
    public final boolean runBuild()
    {
        return runBuild(processors());
    }

    /**
     * Builds with the given number of worker threads
     *
     * @return True if the build succeeded without any problems
     */
    @SuppressWarnings("unchecked")
    public final boolean runBuild(Count threads)
    {
        // Listen to any problems broadcast by the build,
        var issues = new MessageList(message -> !message.status().succeeded());
        issues.listenTo(this);

        // go through each phase in order,
        for (var phase : phases)
        {
            // and if the phase is enabled,
            if (enabled(phase))
            {
                // notify that the phase has started,
                var multicaster = new BuildMulticaster(associatedBuild());
                multicaster.onPhaseStart(phase);

                // run the phase calling all listeners,
                if (associatedBuild().isEnabled(DRY_RUN))
                {
                    announce(" \n$", bannerLine(phase.name()));
                }
                else
                {
                    announce(bannerLine(phase.name()));
                }
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

    /**
     * Records the list of files to remove
     *
     * @param files The files to remove
     * @return This for chaining
     */
    public Builder withAdditionalFiles(Collection<File> files)
    {
        var copy = copy();
        this.files = fileList(this.files).with(files);
        return this;
    }

    /**
     * Records the list of files to remove
     *
     * @param files The files to remove
     * @return This for chaining
     */
    public Builder withFiles(List<File> files)
    {
        var copy = copy();
        this.files = fileList(this.files.with(files));
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRun()
    {
        information("Cleaning $ files", files.count());

        files.forEach(file ->
        {
            file.delete();
            var parent = file.parent();
            if (parent.isEmpty())
            {
                parent.delete();
            }
        });
    }

    /**
     * Returns true if the given phase is enabled
     *
     * @param phase The phase
     * @return True if the phase is enabled
     */
    private boolean enabled(Phase phase)
    {
        return phaseEnabled.getOrDefault(phase, false);
    }

    /**
     * Installs the default phases
     */
    private void installDefaultPhases()
    {
        phases.add(new PhaseStart());
        phases.add(new PhaseClean());
        phases.add(new PhasePrepare());
        phases.add(new PhaseCompile());
        phases.add(new PhaseTest());
        phases.add(new PhaseDocument());
        phases.add(new PhasePackage());
        phases.add(new PhaseIntegrationTest());
        phases.add(new PhaseInstall());
        phases.add(new PhaseDeployPackages());
        phases.add(new PhaseDeployDocumentation());
        phases.add(new PhaseEnd());

        enable(phase("start"));
        enable(phase("end"));
    }
}
