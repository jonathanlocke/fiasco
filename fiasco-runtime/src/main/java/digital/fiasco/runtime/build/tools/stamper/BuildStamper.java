package digital.fiasco.runtime.build.tools.stamper;

import com.telenav.cactus.metadata.BuildName;
import com.telenav.kivakit.conversion.core.time.TimeConverter;
import com.telenav.kivakit.core.collections.list.StringList;
import com.telenav.kivakit.core.string.Formatter;
import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.BuildStructure;
import digital.fiasco.runtime.build.tools.BaseTool;
import digital.fiasco.runtime.build.tools.ToolFactory;
import digital.fiasco.runtime.repository.artifact.Artifact;

import java.time.LocalDate;

import static com.telenav.kivakit.core.collections.list.StringList.stringList;
import static com.telenav.kivakit.core.time.Time.now;
import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;

/**
 * Creates a build.txt file in the root of the classes folder containing information about the build.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class BuildStamper extends BaseTool implements
        BuildStructure,
        ToolFactory
{
    public BuildStamper(Build build)
    {
        super(build);
    }

    @Override
    protected String description()
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

    @Override
    protected void onRun()
    {
        information("Stamping build");

        classesFolder()
                .file(artifact().name() + "-project.properties")
                .saveText(projectProperties().join("\n"));

        classesFolder()
                .file(artifact().name() + "-build.properties")
                .saveText(buildProperties().join("\n"));
    }

    private Artifact artifact()
    {
        return associatedBuild().artifact();
    }

    private StringList buildProperties()
    {
        var git = git();

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

    private StringList projectProperties()
    {
        return stringList("artifact.group = " + artifact().group(),
                "artifact.name = " + artifact().name(),
                "artifact.version = " + artifact().version(),
                "artifact = " + artifact());
    }
}
