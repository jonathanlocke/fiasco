package fiasco.tools.stamper;

import com.telenav.cactus.metadata.BuildName;
import fiasco.BaseBuild;
import fiasco.structure.StructureMixin;
import fiasco.tools.BaseTool;
import fiasco.tools.git.Git;

import java.time.LocalDate;

import static com.telenav.kivakit.core.time.Time.now;

/**
 * Creates a build.txt file in the root of the classes folder containing information about the build.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class BuildStamper extends BaseTool implements
        StructureMixin
{
    public BuildStamper(BaseBuild build)
    {
        super(build);
    }

    @Override
    protected void onRun()
    {
        var artifact = build().artifact();

        try (var out = classesFolder().file(artifact.name() + ".properties").printWriter())
        {
            out.println("artifact.group = " + artifact.group());
            out.println("artifact.name = " + artifact.name());
            out.println("artifact.version = " + artifact.version());
            out.println("artifact = " + artifact);
        }

        var git = new Git(build());

        try (var out = classesFolder().file("build.properties").printWriter())
        {
            out.println("build.time = " + now().asString());
            out.println("build.number = " + BuildName.name(LocalDate.now()));
            out.println("build.name = " + BuildName.toBuildNumber(LocalDate.now()));
            out.println("build.commit.hash = " + git.commitHash());
            out.println("build.commit.time = " + git.commitTime());
        }
    }
}
