package fiasco.tools.builder;

import fiasco.tools.Tools;
import fiasco.tools.BaseTool;

/**
 * @author jonathan
 */
public class BuildStamper extends BaseTool
{
    public BuildStamper(final Tools module)
    {
        super(module);
    }

    @Override
    protected void onRun()
    {
        try (final var out = module().classesFolder().file("build.txt").printWriter())
        {
            out.println(module().project().version());
        }
    }
}
