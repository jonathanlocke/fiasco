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
import com.telenav.kivakit.commandline.ArgumentParser;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.map.ObjectMap;
import com.telenav.kivakit.core.messaging.listeners.MessageList;
import com.telenav.kivakit.core.messaging.messages.status.Problem;
import com.telenav.kivakit.core.messaging.messages.status.Quibble;
import com.telenav.kivakit.core.messaging.messages.status.Warning;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
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
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;
import digital.fiasco.runtime.dependency.artifact.Library;

import java.util.Set;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.string.AsciiArt.bannerLine;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.dependency.artifact.Library.library;

/**
 * Base {@link Application} for Fiasco command-line builds.
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * Build phases are executed in a pre-defined order, as below. Each phase is defined by a class that extends
 * {@link Phase}. Additional phases can be inserted into {@link #phases()} with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}.
 * </p>
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
 * <p><b>Examples</b></p>
 *
 * <table>
 *     <caption>Examples</caption>
 *     <tr>
 *         <td>fiasco</td> <td>&nbsp;&nbsp;</td> <td>show help</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco clean</td> <td>&nbsp;&nbsp;</td> <td>clean targets</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco compile</td> <td>&nbsp;&nbsp;</td> <td>prepare sources and compile</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco clean compile</td> <td>&nbsp;&nbsp;</td> <td>clean targets and compile</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco package</td> <td>&nbsp;&nbsp;</td> <td>compile, test, document and build packages</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco package -test</td> <td>&nbsp;&nbsp;</td> <td>build packages but don't run tests</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco install -test -document</td> <td>&nbsp;&nbsp;</td> <td>install packages but don't run tests or document</td>
 *     </tr>
 * </table>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused", "SwitchStatementWithTooFewBranches",
        "StringConcatenationInLoop" })
public abstract class Build extends Application implements
        ToolFactory,
        BuildEnvironment,
        BuildStructure,
        BuildAssociated,
        BuildListener,
        BuildRepositories
{
    /** The primary artifact being built */
    private ArtifactDescriptor artifactDescriptor;

    /** Metadata about the build */
    private BuildMetadata metadata;

    /** Listeners to call as the build proceeds */
    private ObjectList<BuildListener> buildListeners = list(this);

    /** Libraries to compile with */
    private DependencyList dependencies = dependencyList();

    /** The librarian to manage libraries */
    private final Librarian librarian = listenTo(new Librarian(this));

    /** Phases in order of execution */
    private final PhaseList phases = new PhaseList();

    /** Enable state of each phase */
    private ObjectMap<Phase, Boolean> phaseEnabled = new ObjectMap<>();

    /** The root folder for this build */
    private Folder rootFolder = currentFolder();

    /** If true, describe the build rather than executing it */
    private boolean dryRun = false;

    /**
     * Creates a build
     */
    protected Build()
    {
        installDefaultPhases();
    }

    /**
     * Creates a copy of the given build
     *
     * @param that The build to copy
     */
    protected Build(Build that)
    {
        copyFrom(that);
    }

    /**
     * Adds the given library to the list of libraries for this build
     *
     * @param library The library to add
     */
    public void addLibrary(String library)
    {
        dependencies = dependencies.with(library(library));
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
     * Returns the primary artifact descriptor for this build
     */
    public ArtifactDescriptor artifactDescriptor()
    {
        return artifactDescriptor;
    }

    /**
     * Returns this build
     */
    @Override
    public Build associatedBuild()
    {
        return this;
    }

    /**
     * Returns the list of listeners to this build
     */
    public ObjectList<BuildListener> buildListeners()
    {
        return buildListeners;
    }

    /**
     * Returns the child build at the given path
     *
     * @param path The path to the child build
     */
    public Build childBuild(String path)
    {
        return withChildFolder(path)
                .withArtifactIdentifier(pathTail(path, '/'));
    }

    /**
     * Returns a copy of this build
     */
    public Build copy()
    {
        var copy = typeForClass(getClass()).newInstance();
        copy.copyFrom(this);
        return copy;
    }

    /**
     * Copies the values of the given build into this build
     *
     * @param that The build to copy
     */
    public void copyFrom(Build that)
    {
        this.artifactDescriptor = that.artifactDescriptor;
        this.metadata = that.metadata;
        this.buildListeners = that.buildListeners.copy();
        this.dependencies = that.dependencies.copy();
        this.phaseEnabled = that.phaseEnabled.copy();
        this.rootFolder = that.rootFolder;
        this.dryRun = that.dryRun;
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
    public void disable(Phase phase)
    {
        phaseEnabled.put(phase, false);
    }

    /**
     * Returns true if this build should only describe what will be done
     */
    public boolean dryRun()
    {
        return dryRun;
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
     * The librarian for this build
     *
     * @return The librarian
     */
    public Librarian librarian()
    {
        return librarian;
    }

    /**
     * The libraries required by this build
     *
     * @return The libraries to compile against
     */
    public DependencyList libraries()
    {
        return dependencies;
    }

    /**
     * Sets the metadata for this build
     */
    public void metadata(BuildMetadata metadata)
    {
        this.metadata = metadata;
    }

    /**
     * Returns the metadata for this build
     */
    public BuildMetadata metadata()
    {
        if (metadata == null)
        {
            metadata = new BuildMetadata();
        }
        return metadata;
    }

    /**
     * Returns the list of phases in execution order for this build
     */
    public PhaseList phases()
    {
        return phases;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Project> projects()
    {
        return set(new GsonSerializationProject());
    }

    /**
     * Adds the given artifact to this build's dependencies
     *
     * @param artifact The artifact to add
     */
    public Build requires(Artifact<?> artifact)
    {
        dependencies = dependencies.with(artifact);
        return this;
    }

    /**
     * Returns the root folder of this build
     */
    public Folder rootFolder()
    {
        return rootFolder;
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
                var multicaster = new BuildMulticaster(this);
                multicaster.onPhaseStart(phase);

                // run the phase calling all listeners,
                if (dryRun)
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

    public Build withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.artifactDescriptor = descriptor;
        return copy;
    }

    public Build withArtifactDescriptor(String descriptor)
    {
        var copy = copy();
        copy.artifactDescriptor = ArtifactDescriptor.artifactDescriptor(descriptor);
        return copy;
    }

    public Build withArtifactIdentifier(String identifier)
    {
        var copy = copy();
        copy.artifactDescriptor = artifactDescriptor.withIdentifier(identifier);
        return copy;
    }

    public Build withChildFolder(String child)
    {
        var copy = copy();
        copy.rootFolder = rootFolder.folder(child);
        return copy;
    }

    public Build withDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies;
        return copy;
    }

    public Build withDependencies(Library... dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencyList(dependencies);
        return copy;
    }

    public Build withMetadata(BuildMetadata metadata)
    {
        var copy = copy();
        copy.metadata = metadata;
        return copy;
    }

    public Build withRootFolder(Folder root)
    {
        var copy = copy();
        copy.rootFolder = root;
        return copy;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ObjectList<ArgumentParser<?>> argumentParsers()
    {
        return list(argumentParser(String.class)
                .oneOrMore()
                .converter(new IdentityConverter(this))
                .description("Commands and phases")
                .build());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRun()
    {
        if (!argumentList().isEmpty())
        {
            announce("Building $", rootFolder().name());
            for (var argument : argumentList())
            {
                var value = argument.value();

                switch (value)
                {
                    case "describe" -> dryRun = true;

                    default ->
                    {
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
            }

            runBuild();
        }
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

    /**
     * Returns the phase with the given name
     *
     * @param name The phase name to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    private Phase phase(String name)
    {
        return phases.phase(name);
    }
}
