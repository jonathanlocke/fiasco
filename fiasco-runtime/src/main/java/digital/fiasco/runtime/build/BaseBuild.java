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
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.build.tools.builder.Builder;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

import java.util.Set;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.string.Paths.pathTail;
import static com.telenav.kivakit.filesystem.Folders.currentFolder;
import static digital.fiasco.runtime.build.BuildOption.DRY_RUN;
import static digital.fiasco.runtime.dependency.DependencyList.dependencyList;

/**
 * Base {@link Application} for Fiasco command-line builds.
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * Build phases are executed in a pre-defined order, as below. Each phase is defined by a class that extends
 * {@link Phase}. Additional phases can be inserted into {@link Builder#phases()} with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}.
 * </p>
 *
 * <p>
 * Phases are enabled and disabled with {@link Builder#enable(Phase)} and {@link Builder#disable(Phase)}. The
 * {@link #onRun()} method of the application enables and disables any phase names that were passed from the command
 * line. The phase name itself enables the phase and any dependent phases (for example, "compile" enables the
 * "build-start", "prepare" and "compile" phases). If the phase name is preceded by a dash (for example, -test), the
 * phase is disabled (but not its dependent phases).
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
 * <p><b>Building</b></p>
 *
 * <ul>
 *     <li>{@link #builder()}</li>
 * </ul>
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #requires(DependencyList)}</li>
 *     <li>{@link #requires(Artifact, Artifact...)}</li>
 *     <li>{@link #withDependencies(DependencyList)}</li>
 *     <li>{@link #withDependencies(Artifact[])}</li>
 * </ul>
 *
 * <p><b>Build Listeners</b></p>
 *
 * <ul>
 *     <li>{@link #addBuildListener(BuildListener)}</li>
 *     <li>{@link #buildListeners()}</li>
 * </ul>
 *
 * <p><b>Hierarchy</b></p>
 *
 * <ul>
 *     <li>{@link #childBuild(String)}</li>
 * </ul>
 *
 * <p><b>Functional</b></p>
 *
 * <ul>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #withArtifactDescriptor(String)}</li>
 *     <li>{@link #withArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactIdentifier(String)}</li>
 *     <li>{@link #withDependencies(Artifact[])}</li>
 *     <li>{@link #withDependencies(DependencyList)}</li>
 *     <li>{@link #withRootFolder(Folder)}</li>
 * </ul>
 *
 * <p><b>Properties</b></p>
 *
 * <ul>
 *     <li>{@link #builder()}</li>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #description()}</li>
 *     <li>{@link #metadata()}</li>
 *     <li>{@link #name()}</li>
 *     <li>{@link #rootFolder()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused", "SwitchStatementWithTooFewBranches" })
public abstract class BaseBuild extends Application implements
        Build,
        BuildEnvironment,
        BuildStructured,
        BuildListener,
        BuildRepositories,
        ToolFactory
{
    /** The primary artifact being built */
    private ArtifactDescriptor artifactDescriptor;

    /** Listeners to call as the build proceeds */
    private ObjectList<BuildListener> buildListeners = list(this);

    /** Libraries to compile with */
    private DependencyList dependencies = dependencyList();

    /** The root folder for this build */
    private Folder rootFolder = currentFolder();

    /** Metadata associated with this build */
    private BuildMetadata metadata;

    /** If true, describe the build rather than executing it */
    private ObjectSet<BuildOption> options = set();

    /** The builder to build the project */
    private Builder builder;

    /**
     * Creates a build
     */
    protected BaseBuild()
    {
        addBuildListener(this);
        builder = listenTo(newBuilder());
    }

    /**
     * Creates a copy of the given build
     *
     * @param that The build to copy
     */
    protected BaseBuild(BaseBuild that)
    {
        copyFrom(that);
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

    /**
     * Returns the primary artifact descriptor for this build
     */
    @Override
    public ArtifactDescriptor artifactDescriptor()
    {
        return artifactDescriptor;
    }

    /**
     * Returns the list of listeners to this build
     */
    @Override
    public ObjectList<BuildListener> buildListeners()
    {
        return buildListeners;
    }

    /**
     * Returns the builder for this build
     */
    @Override
    public Builder builder()
    {
        return builder;
    }

    /**
     * Returns the child build at the given path
     *
     * @param path The path to the child build
     */
    @Override
    public Build childBuild(String path)
    {
        return withRootFolder(rootFolder.folder(path))
                .withArtifactIdentifier(pathTail(path, '/'));
    }

    /**
     * Returns a copy of this build
     */
    public BaseBuild copy()
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
    public void copyFrom(BaseBuild that)
    {
        this.artifactDescriptor = that.artifactDescriptor;
        this.buildListeners = that.buildListeners.copy();
        this.dependencies = that.dependencies.copy();
        this.rootFolder = that.rootFolder;
        this.metadata = that.metadata;
        this.builder = that.builder;
        this.options = that.options.copy();
    }

    /**
     * The libraries required by this build
     *
     * @return The libraries to compile against
     */
    @Override
    public DependencyList dependencies()
    {
        return dependencies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return """
                Commands
                                
                  command               description
                  -----------           ---------------------------------------------
                  describe              describe the build rather than running it
                                
                """ + builder.description();
    }

    @Override
    public Build disable(BuildOption option)
    {
        options.remove(option);
        return this;
    }

    /**
     * Disables the given phase from execution during a build
     *
     * @param phase The phase to disable
     */
    @Override
    public void disable(Phase phase)
    {
        builder.disable(phase);
    }

    @Override
    public Build enable(BuildOption option)
    {
        options.add(option);
        return this;
    }

    /**
     * Enables the given phase for execution during a build
     *
     * @param phase The phase to enable
     */
    @Override
    public void enable(Phase phase)
    {
        builder.enable(phase);
    }

    @Override
    public boolean isEnabled(BuildOption option)
    {
        return options.contains(option);
    }

    @Override
    public BuildMetadata metadata()
    {
        return metadata;
    }

    /**
     * Returns the phase with the given name
     *
     * @param name The phase name to look up
     * @return The phase, or null if no phase can be found with the given name
     */
    @Override
    public Phase phase(String name)
    {
        return builder.phase(name);
    }

    /**
     * Returns the list of phases in execution order for this build
     */
    @Override
    public PhaseList phases()
    {
        return builder.phases();
    }

    @Override
    public BaseBuild pinVersion(Artifact<?> artifact, String version)
    {
        librarian().pinVersion(artifact, version);
        return this;
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
     * @param first The artifact to add
     * @param rest The rest of the artifacts to add
     */
    @Override
    public BaseBuild requires(Artifact<?> first, Artifact<?>... rest)
    {
        dependencies = dependencies.with(first, rest);
        return this;
    }

    /**
     * Adds the given dependencies to this build
     *
     * @param dependencies The dependencies to add
     */
    @Override
    public BaseBuild requires(DependencyList dependencies)
    {
        this.dependencies = this.dependencies.with(dependencies);
        return this;
    }

    /**
     * Returns the root folder of this build
     */
    @Override
    public Folder rootFolder()
    {
        return rootFolder;
    }

    /**
     * Returns a copy of this build with the given main artifact descriptor
     *
     * @param descriptor The artifact descriptor
     * @return The copy
     */
    @Override
    public Build withArtifactDescriptor(ArtifactDescriptor descriptor)
    {
        var copy = copy();
        copy.artifactDescriptor = descriptor;
        return copy;
    }

    /**
     * Returns a copy of this build with the given dependencies
     *
     * @param dependencies The dependencies
     * @return The copy
     */
    @Override
    public BaseBuild withDependencies(Artifact<?>... dependencies)
    {
        return withDependencies(dependencyList(dependencies));
    }

    /**
     * Returns a copy of this build with the given dependencies
     *
     * @param dependencies The dependencies
     * @return The copy
     */
    @Override
    public BaseBuild withDependencies(DependencyList dependencies)
    {
        var copy = copy();
        copy.dependencies = dependencies;
        return copy;
    }

    /**
     * Returns a copy of this build with the given root folder
     *
     * @param root The new root folder
     * @return The copy
     */
    @Override
    public BaseBuild withRootFolder(Folder root)
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
                    case "describe" -> enable(DRY_RUN);

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

            builder().runBuild();
        }
    }
}
