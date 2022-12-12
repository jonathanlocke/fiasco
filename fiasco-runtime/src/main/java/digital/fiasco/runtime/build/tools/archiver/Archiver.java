package digital.fiasco.runtime.build.tools.archiver;

import digital.fiasco.runtime.build.Build;
import digital.fiasco.runtime.build.tools.BaseTool;

public class Archiver extends BaseTool
{
    public Archiver(Build build)
    {
        super(build);
    }

    @Override
    protected String description()
    {
        return "Archives resources";
    }

    @Override
    protected void onRun()
    {

    }
}
