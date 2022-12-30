package digital.fiasco.runtime.build.tools.stamper;

import com.telenav.cactus.metadata.BuildName;
import com.telenav.kivakit.conversion.core.time.TimeConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.string.Formatter;
import digital.fiasco.runtime.build.BuildStructured;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.build.tools.ToolFactory;

import java.time.LocalDate;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.time.Time.now;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

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
public class BuildStamper extends BaseTool implements
    BuildStructured,
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
    protected void onRun()
    {
        information("Stamping build");

        targetClassesFolder()
            .file(associatedBuilder().artifactName() + "-project.properties")
            .saveText(projectProperties().join("\n"));

        targetClassesFolder()
            .file(associatedBuilder().artifactName() + "-build.properties")
            .saveText(buildProperties().join("\n"));
    }

    /**
     * Returns build properties as a {@link StringList}
     */
    private StringList buildProperties()
    {
        var git = newGit();

        git.commitHash().run();
        var commitHash = git.output() != null
            ? git.output()
            : "[unknown]";

        git.commitTime().run();
        var commitTime = new TimeConverter(this, ISO_ZONED_DATE_TIME)
            .convert(git.output());

        return stringList("build.time = " + now().asString(),
            "build.number = " + BuildName.name(LocalDate.now()),
            "build.name = " + BuildName.toBuildNumber(LocalDate.now()),
            "build.commit.hash = " + commitHash,
            "build.commit.time = " + commitTime);
    }

    /**
     * Returns project properties as a {@link StringList}
     */
    private StringList projectProperties()
    {
        var descriptor = associatedBuilder().artifactDescriptor();
        return stringList("artifact.group = " + descriptor.group(),
            "artifact.name = " + descriptor.name(),
            "artifact.version = " + descriptor.version(),
            "artifact = " + descriptor);
    }
}
