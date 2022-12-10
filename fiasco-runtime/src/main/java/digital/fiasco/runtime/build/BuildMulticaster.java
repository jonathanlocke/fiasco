package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.collections.list.ObjectList;
import digital.fiasco.runtime.build.phases.Phase;

/**
 * A {@link BuildListener} that calls a list of {@link BuildListener}s
 *
 * @author jonathan
 */
public class BuildMulticaster implements
        BuildListener,
        BuildStructure
{
    private final Build build;

    public BuildMulticaster(Build build)
    {
        this.build = build;
    }

    @Override
    public Build attachedToBuild()
    {
        return build;
    }

    public ObjectList<BuildListener> buildListeners()
    {
        return build.buildListeners();
    }

    @Override
    public void onBuildStart()
    {
        buildListeners().forEach(BuildListener::onBuildStart);
    }

    @Override
    public void onBuildStarted()
    {
        buildListeners().forEach(BuildListener::onBuildStarted);
    }

    @Override
    public void onBuildStarting()
    {
        buildListeners().forEach(BuildListener::onBuildStarting);
    }

    @Override
    public void onClean()
    {
        buildListeners().forEach(BuildListener::onClean);
    }

    @Override
    public void onCleaned()
    {
        buildListeners().forEach(BuildListener::onCleaned);
    }

    @Override
    public void onCleaning()
    {
        buildListeners().forEach(BuildListener::onCleaning);
    }

    @Override
    public void onCompile()
    {
        buildListeners().forEach(BuildListener::onCompile);
    }

    @Override
    public void onCompiled()
    {
        buildListeners().forEach(BuildListener::onCompiled);
    }

    @Override
    public void onCompiling()
    {
        buildListeners().forEach(BuildListener::onCompiling);
    }

    @Override
    public void onDeployDocumentation()
    {
        buildListeners().forEach(BuildListener::onDeployDocumentation);
    }

    @Override
    public void onDeployPackages()
    {
        buildListeners().forEach(BuildListener::onDeployPackages);
    }

    @Override
    public void onDeployedDocumentation()
    {
        buildListeners().forEach(BuildListener::onDeployedDocumentation);
    }

    @Override
    public void onDeployedPackages()
    {
        buildListeners().forEach(BuildListener::onDeployedPackages);
    }

    @Override
    public void onDeployingDocumentation()
    {
        buildListeners().forEach(BuildListener::onDeployingDocumentation);
    }

    @Override
    public void onDeployingPackages()
    {
        buildListeners().forEach(BuildListener::onDeployingPackages);
    }

    @Override
    public void onDocument()
    {
        buildListeners().forEach(BuildListener::onDocument);
    }

    @Override
    public void onDocumented()
    {
        buildListeners().forEach(BuildListener::onDocumented);
    }

    @Override
    public void onDocumenting()
    {
        buildListeners().forEach(BuildListener::onDocumenting);
    }

    @Override
    public void onInstallPackages()
    {
        buildListeners().forEach(BuildListener::onInstallPackages);
    }

    @Override
    public void onInstalledPackages()
    {
        buildListeners().forEach(BuildListener::onInstalledPackages);
    }

    @Override
    public void onInstallingPackages()
    {
        buildListeners().forEach(BuildListener::onInstallingPackages);
    }

    @Override
    public void onIntegrationTest()
    {
        buildListeners().forEach(BuildListener::onIntegrationTest);
    }

    @Override
    public void onIntegrationTested()
    {
        buildListeners().forEach(BuildListener::onIntegrationTested);
    }

    @Override
    public void onIntegrationTesting()
    {
        buildListeners().forEach(BuildListener::onIntegrationTesting);
    }

    @Override
    public void onPackage()
    {
        buildListeners().forEach(BuildListener::onPackage);
    }

    @Override
    public void onPackaged()
    {
        buildListeners().forEach(BuildListener::onPackaged);
    }

    @Override
    public void onPackaging()
    {
        buildListeners().forEach(BuildListener::onPackaging);
    }

    @Override
    public void onPhaseEnd(Phase phase)
    {
        buildListeners().forEach(at -> at.onPhaseEnd(phase));
    }

    @Override
    public void onPhaseStart(Phase phase)
    {
        buildListeners().forEach(at -> at.onPhaseStart(phase));
    }

    @Override
    public void onPrepareResources()
    {
        buildListeners().forEach(BuildListener::onPrepareResources);
    }

    @Override
    public void onPrepareSources()
    {
        buildListeners().forEach(BuildListener::onPrepareSources);
    }

    @Override
    public void onPrepareTestResources()
    {
        buildListeners().forEach(BuildListener::onPrepareTestResources);
    }

    @Override
    public void onPrepareTestSources()
    {
        buildListeners().forEach(BuildListener::onPrepareTestSources);
    }

    @Override
    public void onPrepareTestedResources()
    {
        buildListeners().forEach(BuildListener::onPrepareTestedResources);
    }

    @Override
    public void onPreparedResources()
    {
        buildListeners().forEach(BuildListener::onPreparedResources);
    }

    @Override
    public void onPreparedSources()
    {
        buildListeners().forEach(BuildListener::onPreparedSources);
    }

    @Override
    public void onPreparedTestSources()
    {
        buildListeners().forEach(BuildListener::onPreparedTestSources);
    }

    @Override
    public void onPreparingResources()
    {
        buildListeners().forEach(BuildListener::onPreparingResources);
    }

    @Override
    public void onPreparingSources()
    {
        buildListeners().forEach(BuildListener::onPreparingSources);
    }

    @Override
    public void onPreparingTestResources()
    {
        buildListeners().forEach(BuildListener::onPreparingTestResources);
    }

    @Override
    public void onPreparingTestSources()
    {
        buildListeners().forEach(BuildListener::onPreparingTestSources);
    }

    @Override
    public void onTest()
    {
        buildListeners().forEach(BuildListener::onTest);
    }

    @Override
    public void onTestCompile()
    {
        buildListeners().forEach(BuildListener::onTestCompile);
    }

    @Override
    public void onTestCompiled()
    {
        buildListeners().forEach(BuildListener::onTestCompiled);
    }

    @Override
    public void onTestCompiling()
    {
        buildListeners().forEach(BuildListener::onTestCompiling);
    }

    @Override
    public void onTested()
    {
        buildListeners().forEach(BuildListener::onTested);
    }

    @Override
    public void onTesting()
    {
        buildListeners().forEach(BuildListener::onTesting);
    }
}
