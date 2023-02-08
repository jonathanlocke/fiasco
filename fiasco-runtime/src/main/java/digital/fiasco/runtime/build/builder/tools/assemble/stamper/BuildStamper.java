package digital.fiasco.runtime.build.builder.tools.assemble.stamper;

import com.telenav.cactus.metadata.BuildName;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.string.Formatter;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.tools.BaseTool;
import digital.fiasco.runtime.build.builder.tools.ToolFactory;
import digital.fiasco.runtime.build.builder.tools.toolchain.git.GitLocalTimeConverter;
import digital.fiasco.runtime.build.environment.BuildStructure;

import java.time.LocalDate;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.time.Time.now;

/**
 * Creates files in the root of the classes folder containing information about the project and the build. Both project
 * properties and build properties files are prefixed with the application or project name to ensure that these files
 * don't conflict in shaded JARs.
 *
 * <p><b>Build Properties</b></p>
 *
 * <p>Build properties files contain content similar to this:</p>
 *
 * <pre>
 * build-date = 2022.12.21
 * build-name = happy mouse
 * build-number = 746
 * commit-long-hash = 1bb7eba5cc7527e76950d6edbed931bbadb16dd6
 * commit-timestamp = 2022-12-21T22:10:26Z[GMT]
 * no-local-modifications = false</pre>
 *
 * <p><b>Project Properties</b></p>
 *
 * <p>Project properties files look similar to this:</p>
 *
 * <pre>
 * project-name=fiasco-runtime
 * project-version=0.9.0
 * project-group-id=digital.fiasco
 * project-artifact-id=fiasco-runtime</pre>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings({ "unused", "SpellCheckingInspection" })
public class BuildStamper extends BaseTool<BuildStamper> implements
    BuildStructure,
    ToolFactory
{
    /**
     * Creates a build stamper associated with this builder
     *
     * @param builder The builder
     */
    public BuildStamper(Builder builder)
    {
        super(builder);
    }

    public BuildStamper(BuildStamper that)
    {
        super(that);
    }

    @Override
    public BuildStamper copy()
    {
        return new BuildStamper(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String description()
    {
        return Formatter.format("""
                BuildStamper:
                  project properties:
                $
                  build properties:
                $
                """,
            projectProperties().indented(4),
            buildProperties().indented(4));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRun()
    {
        information("Stamping build");

        var name = associatedBuilder().descriptor().artifactName().name().replaceAll("\\.", "-");

        var projectProperties = targetClassesFolder()
            .file(name + "-project.properties");
        var buildProperties = targetClassesFolder()
            .file(name + "-build.properties");

        step(() -> projectProperties.saveText(projectProperties().join("\n")),
            "Writing $", projectProperties);

        step(() -> buildProperties.saveText(buildProperties().join("\n")),
            "Writing $", buildProperties);
    }

    /**
     * Returns build properties as a {@link StringList}
     */
    private StringList buildProperties()
    {
        var git = newGit().commitHash();
        git.run();

        var commitHash = git.output() != null
            ? git.output()
            : "[unknown]";

        git = newGit().commitTime();
        git.run();

        var commitTime = new GitLocalTimeConverter(this)
            .convert(git.output());

        return stringList("build.time = " + now().asString(),
            "build.name = " + BuildName.name(LocalDate.now()),
            "build.number = " + BuildName.toBuildNumber(LocalDate.now()),
            "build.commit.hash = " + commitHash,
            "build.commit.time = " + commitTime);
    }

    /**
     * Returns project properties as a {@link StringList}
     */
    private StringList projectProperties()
    {
        var descriptor = associatedBuilder().descriptor();
        return stringList("artifact.group = " + descriptor.group(),
            "artifact.name = " + descriptor.artifactName(),
            "artifact.version = " + descriptor.version(),
            "artifact = " + descriptor);
    }
}
