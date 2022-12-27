package digital.fiasco.runtime.build.tools.builder;

import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.value.count.Count;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.phases.BasePhase;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.phases.standard.StandardPhases;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.build.tools.librarian.Librarian;

import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static digital.fiasco.runtime.build.BuildOption.DRY_RUN;

/**
 * Builds a project by executing a series of phases. A {@link Build} may have multiple {@link Builder}s, to achieve a
 * multi-project build or to achieve two different kinds of builds.
 *
 * <p><b>Building</b></p>
 *
 * <ul>
 *     <li>{@link #build()}</li>
 *     <li>{@link #build(Count)}</li>
 * </ul>
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #librarian()}</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * By default, phases are executed in a pre-defined order, as shown below. Each phase is defined by a class that implements
 * {@link Phase} (and normally extends {@link BasePhase}). Additional phases can be inserted into the {@link #phases()} list with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}. A specific
 * phase can be replaced with a new definition with {@link PhaseList#replacePhase(String, Phase)}. Alternatively, an
 * entirely new phase list can be installed with {@link #withPhases(PhaseList)}.
 * </p>
 *
 * <ul>
 *     <li>{@link #phases()}</li>
 *     <li>{@link #withPhases(PhaseList)}</li>
 * </ul>
 *
 * <p><b>Attaching Actions to Phases</b></p>
 *
 * <p>
 * A phase can be retrieved by name with {@link #phase(String)} and an arbitrary {@link Runnable}
 * can be attached to run before, during or after the phase executes.
 * </p>
 *
 * <p>
 * Phases are enabled and disabled with {@link #enable(Phase)} and {@link #disable(Phase)}. The {@link #onRun()} method
 * of the application enables and disables any phase names that were passed from the command line. The phase name itself
 * enables the phase and any dependent phases (for example, "compile" enables the "start", "prepare" and "compile"
 * phases). If the phase name is preceded by a dash (for example, -test), the phase is disabled (but not its dependent
 * phases).
 * </p>
 *
 * <ul>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #disable(Phase)}</li>
 * </ul>
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
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "StringConcatenationInLoop", "UnusedReturnValue" })
public class Builder extends BaseTool implements ToolFactory
{
    /** Phases in order of execution */
    private PhaseList phases = new StandardPhases(this);

    /** The librarian to manage libraries */
    private Librarian librarian = listenTo(newLibrarian());

    /**
     * Creates a builder for the given build
     *
     * @param build The associated build for this builder
     */
    public Builder(Build build)
    {
        super(build);
    }

    /**
     * Creates a copy of the give builder
     *
     * @param that The builder to copy
     */
    public Builder(Builder that)
    {
        super(that.associatedBuild());
        this.phases = that.phases.copy();
        this.librarian = that.librarian;
    }

    /**
     * Builds with one thread for each processor
     *
     * @return True if the build succeeded without any problems
     */
    public final boolean build()
    {
        return build(processors());
    }

    /**
     * Builds with the given number of worker threads
     *
     * @return True if the build succeeded without any problems
     */
    @SuppressWarnings("unchecked")
    public final boolean build(Count threads)
    {
        // Listen to any problems broadcast by the build,
        var issues = new MessageList(message -> !message.status().succeeded());
        issues.listenTo(this);

        // go through each phase in order,
        for (var phase : phases)
        {
            // and if the phase is enabled,
            if (phases.isEnabled(phase))
            {
                // notify that the phase has started,
                phase.internalOnBefore();

                // run the phase calling all listeners,
                if (associatedBuild().isEnabled(DRY_RUN))
                {
                    announce(" \n$", bannerLine(phase.name()));
                }
                else
                {
                    announce(bannerLine(phase.name()));
                }
                phase.run();

                // notify that the phase has ended,
                phase.internalOnAfter();
            }
        }

        // then collect statistics and display them,
        var statistics = issues.statistics(Problem.class, Warning.class, Quibble.class);
        information(statistics.titledBox("Build Results"));

        // and return true if there are no problems (or worse).
        return issues.countWorseThanOrEqualTo(Problem.class).isZero();
    }

    @Override
    public Builder builder()
    {
        return this;
    }

    public Builder copy()
    {
        return new Builder(this);
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
     * Disables execution of the given phase
     *
     * @param phase The phase to disable
     */
    public void disable(Phase phase)
    {
        phases.disable(phase);
    }

    /**
     * Enables execution of the given phase
     *
     * @param phase The phase to enables
     */
    public void enable(Phase phase)
    {
        phases.enable(phase);
    }

    /**
     * Returns true if the given phase is enabled for execution
     *
     * @param phase The phase
     */
    public boolean isEnabled(Phase phase)
    {
        return phases.isEnabled(phase);
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
     * Returns the phase with the given name
     *
     * @param name The phase name
     * @return The phase
     */
    public Phase phase(String name)
    {
        return phases.phase(name);
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
     * Returns a copy of this builder with the given phases
     *
     * @param phases The list of phases this builder should execute
     * @return A copy of this builder with the given phases
     */
    public Builder withPhases(PhaseList phases)
    {
        var copy = copy();
        copy.phases = phases;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRun()
    {
        build();
    }
}
