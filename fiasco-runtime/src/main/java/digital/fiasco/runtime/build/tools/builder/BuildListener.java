package digital.fiasco.runtime.build.tools.builder;

import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.tools.ToolFactory;

import static com.telenav.kivakit.interfaces.comparison.Matcher.matchAll;

/**
 * A listener that is called as the build proceeds
 *
 * @author Jonathan Locke
 */
public interface BuildListener extends ToolFactory
{
    default void onBuildStart()
    {
    }

    default void onBuildStarted()
    {
    }

    default void onBuildStarting()
    {
    }

    default void onClean()
    {
        newCleaner().withFiles(targetFolder().nestedFiles(matchAll())).run();
    }

    default void onCleaned()
    {
    }

    default void onCleaning()
    {
    }

    default void onCompile()
    {
        newCompiler().withSources(sourceMainJavaSources()).run();
        newStamper().run();
    }

    default void onCompiled()
    {
    }

    default void onCompiling()
    {
    }

    default void onDeployDocumentation()
    {
    }

    default void onDeployPackages()
    {
    }

    default void onDeployedDocumentation()
    {
    }

    default void onDeployedPackages()
    {
    }

    default void onDeployingDocumentation()
    {
    }

    default void onDeployingPackages()
    {
    }

    default void onDocument()
    {
    }

    default void onDocumented()
    {
    }

    default void onDocumenting()
    {
    }

    default void onInstallPackages()
    {
    }

    default void onInstalledPackages()
    {
    }

    default void onInstallingPackages()
    {
    }

    default void onIntegrationTest()
    {
    }

    default void onIntegrationTested()
    {
    }

    default void onIntegrationTesting()
    {
    }

    default void onPackage()
    {
    }

    default void onPackaged()
    {
    }

    default void onPackaging()
    {
    }

    default void onPhaseEnd(Phase phase)
    {
    }

    default void onPhaseStart(Phase phase)
    {
    }

    default void onPrepareResources()
    {
        newCopier().withSourceFolder(sourceMainResourcesFolder())
                .withTargetFolder(targetClassesFolder())
                .with("**/*")
                .run();

        newCopier().withSourceFolder(sourceTestResourcesFolder())
                .withTargetFolder(targetTestClassesFolder())
                .with("**/*")
                .run();
    }

    default void onPrepareSources()
    {
    }

    default void onPrepareTestResources()
    {
    }

    default void onPrepareTestSources()
    {
    }

    default void onPrepareTestedResources()
    {
    }

    default void onPreparedResources()
    {
    }

    default void onPreparedSources()
    {
    }

    default void onPreparedTestSources()
    {
    }

    default void onPreparingResources()
    {
    }

    default void onPreparingSources()
    {
    }

    default void onPreparingTestResources()
    {
    }

    default void onPreparingTestSources()
    {
    }

    default void onTest()
    {
    }

    default void onTestCompile()
    {
    }

    default void onTestCompiled()
    {
    }

    default void onTestCompiling()
    {
    }

    default void onTested()
    {
    }

    default void onTesting()
    {
    }
}
