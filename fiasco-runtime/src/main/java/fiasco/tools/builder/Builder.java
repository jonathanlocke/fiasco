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
        build().buildListeners().forEach(BuildListener::onArchiving);
        build().archiver().run();
        build().buildListeners().forEach(BuildListener::onArchived);
    }

    public void compile()
    {
        build().buildListeners().forEach(BuildListener::onCompiling);
        build().compiler().run();
        build().buildListeners().forEach(BuildListener::onCompiled);
    }

    public void deploy()
    {
        build().buildListeners().forEach(BuildListener::onDeploying);
        build().librarian().deploy(null).run();
        build().buildListeners().forEach(BuildListener::onDeployed);
    }

    public void install()
    {
        build().buildListeners().forEach(BuildListener::onInstalling);
        build().librarian().install(null);
        build().buildListeners().forEach(BuildListener::onInstalled);
    }

    public void listenTo(BuildListener listener)
    {
        build().buildListeners().add(listener);
    }

    public void onCompiled(Runnable code)
    {
        listenTo(new BuildListener()
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
        listenTo(new BuildListener()
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
        build().buildListeners().forEach(BuildListener::onBuilding);
        onBuild();
        build().buildListeners().forEach(BuildListener::onBuilt);
    }

    public void test()
    {
        build().buildListeners().forEach(BuildListener::onTesting);
        build().tester().run();
        build().buildListeners().forEach(BuildListener::onTested);
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
