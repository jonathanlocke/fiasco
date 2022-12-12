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
import com.telenav.kivakit.core.string.Paths;
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
import digital.fiasco.runtime.build.phases.PhasePackage;
import digital.fiasco.runtime.build.phases.PhasePrepare;
import digital.fiasco.runtime.build.phases.PhaseStart;
import digital.fiasco.runtime.build.phases.PhaseTest;
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.repository.Library;
import digital.fiasco.runtime.repository.artifact.ArtifactDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.google.common.base.Strings.repeat;
import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.ensure.Ensure.ensureNotNull;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;
import static digital.fiasco.runtime.repository.Library.library;
import static digital.fiasco.runtime.repository.artifact.ArtifactDescriptor.parseArtifactDescriptor;

/**
 * Base {@link Application} for Fiasco command-line builds.
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * Build phases are executed in a pre-defined order, as below. Each phase is defined by a class that extends
 * {@link Phase}. Additional phases can be inserted with {@link #addPhaseAfter(String, Phase)} and
 * {@link #addPhaseBefore(String, Phase)}.
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
 *     <tr>
 *         <td>fiasco</td> <td>&nbsp;&nbsp;</td> </td><td>show help</td>
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
 * @author jonathan
 */
@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused", "SwitchStatementWithTooFewBranches" })
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
    private DependencyList<Library> dependencies = dependencyList();

    /** The librarian to manage libraries */
    private final Librarian librarian = listenTo(new Librarian(this));

    /** Maps from the name of a phase to the {@link Phase} object */
    private ObjectMap<String, Phase> nameToPhase = new ObjectMap<>();

    /** Phases in order of execution */
    private ObjectList<Phase> orderedPhases = list();

    /** Enable state of each phase */
    private ObjectMap<Phase, Boolean> phaseEnabled = new ObjectMap<>();

    /** The root folder for this build */
    private Folder rootFolder = currentFolder();

    /** If true, describe the build rather than executing it */
    private boolean describe = false;

    protected Build()
    {
        // Install default build phases
        onInstallPhases();
    }

    protected Build(Build that)
    {
        copy(that);
    }

    /**
     * Adds the given library to the list of libraries for this build
     *
     * @param library The library to add
     */
    public void addLibrary(String library)
    {
        libraries().add(library(library));
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

    public ArtifactDescriptor artifactDescriptor()
    {
        return artifactDescriptor;
    }

    @Override
    public Build associatedBuild()
    {
        return this;
    }

    public ObjectList<BuildListener> buildListeners()
    {
        return buildListeners;
    }

    public void copy(Build that)
    {
        this.artifactDescriptor = that.artifactDescriptor;
        this.metadata = that.metadata.copy();
        this.buildListeners = that.buildListeners.copy();
        this.dependencies = that.dependencies.copy();
        this.nameToPhase = that.nameToPhase.copy();
        this.orderedPhases = that.orderedPhases.copy();
        this.phaseEnabled = that.phaseEnabled.copy();
        this.rootFolder = that.rootFolder;
        this.describe = that.describe;
    }

    public Build copy()
    {
        var copy = typeForClass(getClass()).newInstance();
        copy.copy(this);
        return copy;
    }

    public boolean describe()
    {
        return describe;
    }

    @Override
    public String description()
    {
        return """
                Commands
                                
                  command               purpose
                  -----------           ---------------------------------------------
                  describe              describe the build rather than running it
                                
                Phase arguments will be enabled, those preceded by a dash will be disabled.
                            
                  phase                 purpose
                  -----------           ---------------------------------------------
                  clean                 cleans targets
                  prepare               prepares resources and sources
                  compile               builds sources
                  test                  runs tests
                  document              builds documentation
                  package               creates packages
                  integration-test      runs integration tests
                  install               installs packages
                  deploy-packages       deploys packages
                  deploy-documentation  deploys documentation
                """;
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
    public DependencyList<Library> libraries()
    {
        return dependencies;
    }

    public void metadata(BuildMetadata metadata)
    {
        this.metadata = metadata;
    }

    public BuildMetadata metadata()
    {
        return metadata;
    }

    public Phase phase(String name)
    {
        return ensureNotNull(nameToPhase.get(name), "Could not find phase: $", name);
    }

    public Build project(String path)
    {
        return withChildFolder(path)
                .withArtifactIdentifier(Paths.pathTail(path, '/'));
    }

    @Override
    public Set<Project> projects()
    {
        return set(new GsonSerializationProject());
    }

    public ToolFactory requires(Library library)
    {
        dependencies.add(library);
        return this;
    }

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
        for (var phase : orderedPhases)
        {
            // and if the phase is enabled,
            if (enabled(phase))
            {
                // notify that the phase has started,
                var multicaster = new BuildMulticaster(this);
                multicaster.onPhaseStart(phase);

                // run the phase calling all listeners,
                if (describe)
                {
                    announce(" \n$ $ $", bar("="), phase.name(), bar("="));
                }
                else
                {
                    announce("$ $ $", bar("="), phase.name(), bar("="));
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
        copy.artifactDescriptor(descriptor);
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

    public Build withDependencies(DependencyList<Library> dependencies)
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

    @Override
    protected ObjectList<ArgumentParser<?>> argumentParsers()
    {
        return list(argumentParser(String.class)
                .oneOrMore()
                .converter(new IdentityConverter(this))
                .description("Commands and phases")
                .build());
    }

    protected void artifactDescriptor(ArtifactDescriptor artifact)
    {
        this.artifactDescriptor = artifact;
    }

    protected void artifactDescriptor(String descriptor)
    {
        artifactDescriptor = parseArtifactDescriptor(this, descriptor);
    }

    protected final void onInstallPhases()
    {
        addPhase(new PhaseStart());
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
        addPhase(new PhaseEnd());

        enable(phase("start"));
        enable(phase("end"));
    }

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
                    case "describe" -> describe = true;

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

    @NotNull
    private static String bar(String text)
    {
        return repeat(text, 8);
    }

    private boolean enabled(Phase phase)
    {
        return phaseEnabled.getOrDefault(phase, false);
    }
}
