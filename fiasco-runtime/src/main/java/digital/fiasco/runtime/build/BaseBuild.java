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
import com.telenav.kivakit.commandline.SwitchParser;
import com.telenav.kivakit.conversion.core.language.IdentityConverter;
import com.telenav.kivakit.core.collections.list.ObjectList;
import com.telenav.kivakit.core.collections.set.ObjectSet;
import com.telenav.kivakit.core.function.Result;
import com.telenav.kivakit.core.project.Project;
import com.telenav.kivakit.core.value.count.Count;
import com.telenav.kivakit.serialization.gson.GsonSerializationProject;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.execution.BuildExecutor;
import digital.fiasco.runtime.build.metadata.BuildMetadata;
import digital.fiasco.runtime.repository.remote.server.serialization.FiascoGsonFactory;

import java.util.Set;

import static com.telenav.kivakit.commandline.ArgumentParser.argumentParser;
import static com.telenav.kivakit.commandline.SwitchParsers.countSwitchParser;
import static com.telenav.kivakit.core.collections.list.ObjectList.list;
import static com.telenav.kivakit.core.collections.set.ObjectSet.set;
import static com.telenav.kivakit.core.language.reflection.Type.typeForClass;
import static com.telenav.kivakit.core.value.count.Count._0;
import static com.telenav.kivakit.core.value.count.Count._16;
import static com.telenav.kivakit.core.vm.JavaVirtualMachine.javaVirtualMachine;
import static digital.fiasco.runtime.build.settings.BuildSettings.buildSettings;

/**
 * {@inheritDoc}
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "SameParameterValue", "UnusedReturnValue", "unused" })
public abstract class BaseBuild extends Application implements Build
{
    /** Metadata associated with this build */
    private BuildMetadata metadata;

    /** Switch parser for -builder-threads=[count] */
    private final SwitchParser<Count> BUILDER_THREADS = countSwitchParser(this, "builder-threads",
        "The number of threads to use when building")
        .optional()
        .defaultValue(javaVirtualMachine().processors())
        .build();

    /** Switch parser for -artifact-resolver-threads=[count] */
    private final SwitchParser<Count> ARTIFACT_RESOLVER_THREADS = countSwitchParser(this, "artifact-resolver-threads",
        "The number of threads to use when resolving artifacts")
        .optional()
        .defaultValue(_16)
        .build();

    /** The root builder for this build (once configured) */
    private final Builder rootBuilder;

    /**
     * Creates a build
     */
    public BaseBuild()
    {
        rootBuilder = onConfigureBuild(newBuilder());
    }

    /**
     * Creates a copy of the given build
     *
     * @param that The build to copy
     */
    protected BaseBuild(BaseBuild that)
    {
        this.metadata = that.metadata;
        this.rootBuilder = that.rootBuilder;
    }

    /**
     * Returns a copy of this build
     */
    @Override
    public BaseBuild copy()
    {
        var copy = typeForClass(getClass()).newInstance();
        copy.metadata = this.metadata;
        return copy;
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
                            
            """ + newBuilder().description();
    }

    /**
     * Executes this build using a {@link BuildExecutor}
     *
     * @return The builder results (one for each project)
     */
    public ObjectList<Result<Builder>> executeBuild()
    {
        // Create the root builder,
        var root = newBuilder().withArtifactDescriptor(metadata.descriptor());

        // configure and run the build,
        return new BuildExecutor(this, rootBuilder).build();
    }

    @Override
    public BuildMetadata metadata()
    {
        return metadata;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Set<Project> projects()
    {
        return set(new GsonSerializationProject());
    }

    /**
     * Returns the root builder for this build
     *
     * @return The root builder
     */
    public Builder rootBuilder()
    {
        return rootBuilder;
    }

    @Override
    public boolean shouldDescribe()
    {
        return false;
    }

    @Override
    public boolean shouldDescribeAndExecute()
    {
        return false;
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
            .description("Build options and phases")
            .build());
    }

    protected Builder newBuilder()
    {
        var builder = new Builder(this);
        return builder
            .withSettings(buildSettings(builder)
                .withBuilderThreads(get(BUILDER_THREADS))
                .withArtifactResolverThreads(get(ARTIFACT_RESOLVER_THREADS)))
            .withParsedCommandLine(commandLine());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final void onRun()
    {
        // Execute the build,
        var results = executeBuild();

        // then show the results.
        var problems = _0;
        for (var at : results)
        {
            at.messages().broadcastTo(this);
            problems = problems.plus(at.messages().problems());
        }

        information("Build completed with $ problems", problems);
    }

    @Override
    protected void onSerializationInitialize()
    {
        super.onSerializationInitialize();

        register(new FiascoGsonFactory());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ObjectSet<SwitchParser<?>> switchParsers()
    {
        return super.switchParsers().with(
            BUILDER_THREADS,
            ARTIFACT_RESOLVER_THREADS);
    }
}
