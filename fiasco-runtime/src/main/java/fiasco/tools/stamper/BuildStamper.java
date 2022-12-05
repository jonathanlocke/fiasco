package fiasco.tools.stamper;

import fiasco.BaseBuild;
import fiasco.structure.StructureMixin;
import fiasco.tools.BaseTool;

/**
 * Creates a build.txt file in the root of the classes folder containing information about the build.
 *
 * @author jonathan
 */
@SuppressWarnings("unused")
public class BuildStamper extends BaseTool implements StructureMixin
{
    public BuildStamper(BaseBuild build)
    {
        super(build);
    }

    @Override
    protected void onRun()
    {
        try (var out = classesFolder().file("build.txt").printWriter())
        {
            out.println("version = " + build().artifact().version());
        }
    }
}
