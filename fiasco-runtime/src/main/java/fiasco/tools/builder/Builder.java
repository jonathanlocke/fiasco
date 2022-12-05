package fiasco.tools.builder;

import fiasco.BaseBuild;
import fiasco.tools.BaseTool;

/**
 * A build performs a set of actions on a collection of projects and other builds to produce a set of artifacts.
 *
 * @author jonathan)
 */
@SuppressWarnings("unused")
public class Builder extends BaseTool
{
    public Builder(BaseBuild build)
    {
        super(build);
    }

    public void archive()
    {
        build().notify(BuildListener::onArchiving);
        build().archiver().run();
        build().notify(BuildListener::onArchived);
    }

    public void compile()
    {
        build().notify(BuildListener::onCompiling);
        build().compiler().run();
        build().notify(BuildListener::onCompiled);
    }

    public void deploy()
    {
        build().notify(BuildListener::onDeploying);
        build().librarian().deploy(null).run();
        build().notify(BuildListener::onDeployed);
    }

    public void install()
    {
        build().notify(BuildListener::onInstalling);
        build().librarian().install(null);
        build().notify(BuildListener::onInstalled);
    }

    public void onCompiled(Runnable code)
    {
        build().addListener(new BuildListener()
        {
            @Override
            public void onCompiled()
            {
                code.run();
            }
        });
    }

    public void onCompiling(Runnable code)
    {
        build().addListener(new BuildListener()
        {
            @Override
            public void onCompiling()
            {
                code.run();
            }
        });
    }

    @Override
    public void onRun()
    {
        build().notify(BuildListener::onBuilding);
        onBuild();
        build().notify(BuildListener::onBuilt);
    }

    public void test()
    {
        build().notify(BuildListener::onTesting);
        build().tester().run();
        build().notify(BuildListener::onTested);
    }

    protected void onBuild()
    {
        compile();
        test();
        archive();
        install();
        deploy();
    }
}
