package digital.fiasco.runtime.build;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.core.version.Version;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.interfaces.naming.Named;
import com.telenav.kivakit.interfaces.string.Described;
import digital.fiasco.runtime.build.phases.Phase;
import digital.fiasco.runtime.build.phases.PhaseList;
import digital.fiasco.runtime.build.tools.builder.Builder;
import digital.fiasco.runtime.dependency.DependencyList;
import digital.fiasco.runtime.dependency.artifact.Artifact;
import digital.fiasco.runtime.dependency.artifact.ArtifactDescriptor;

/**
 * Defines a Fiasco build.
 *
 * <p><b>Build Phases</b></p>
 *
 * <p>
 * Build phases are executed in a pre-defined order, as below. Each phase is defined by a class that extends
 * {@link Phase}. Additional phases can be inserted into {@link Builder#phases()} with
 * {@link PhaseList#addPhaseAfter(String, Phase)} and {@link PhaseList#addPhaseBefore(String, Phase)}.
 * </p>
 *
 * <p>
 * Phases are enabled and disabled with {@link Builder#enable(Phase)} and {@link Builder#disable(Phase)}. The onRun()
 * method of the build application enables and disables any phase names that were passed from the command line. The
 * phase name itself enables the phase and any dependent phases (for example, "compile" enables the "build-start",
 * "prepare" and "compile" phases). If the phase name is preceded by a dash (for example, -test), the phase is disabled
 * (but not its dependent phases).
 * </p>
 *
 * <ol>
 *     <li>start</li>
 *     <li>clean</li>
 *     <li>prepare</li>
 *     <li>compile</li>
 *     <li>test</li>
 *     <li>document</li>
 *     <li>package</li>
 *     <li>integration-test</li>
 *     <li>install</li>
 *     <li>deploy-packages</li>
 *     <li>deploy-documentation</li>
 *     <li>end</li>
 * </ol>
 *
 * <p><b>Examples</b></p>
 *
 * <table>
 *     <caption>Examples</caption>
 *     <tr>
 *         <td>fiasco</td> <td>&nbsp;&nbsp;</td> <td>show help</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco clean</td> <td>&nbsp;&nbsp;</td> <td>clean targets</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco compile</td> <td>&nbsp;&nbsp;</td> <td>prepare sources and compile</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco clean compile</td> <td>&nbsp;&nbsp;</td> <td>clean targets and compile</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco package</td> <td>&nbsp;&nbsp;</td> <td>compile, test, document and build packages</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco package -test</td> <td>&nbsp;&nbsp;</td> <td>build packages but don't run tests</td>
 *     </tr>
 *     <tr>
 *         <td>fiasco install -test -document</td> <td>&nbsp;&nbsp;</td> <td>install packages but don't run tests or document</td>
 *     </tr>
 * </table>
 *
 * <p><b>Building</b></p>
 *
 * <ul>
 *     <li>{@link #builder()}</li>
 *     <li>{@link #childBuild(String)}</li>
 *     <li>{@link #copy()}</li>
 *     <li>{@link #disable(BuildOption)}</li>
 *     <li>{@link #enable(BuildOption)}</li>
 *     <li>{@link #isEnabled(BuildOption)}</li>
 *     <li>{@link #rootFolder()}</li>
 *     <li>{@link #withRootFolder(Folder)}</li>
 * </ul>
 *
 * <p><b>Artifact</b></p>
 *
 * <ul>
 *     <li>{@link #artifactDescriptor()}</li>
 *     <li>{@link #artifactName()}</li>
 *     <li>{@link #artifactVersion()}</li>
 *     <li>{@link #withArtifactDescriptor(ArtifactDescriptor)}</li>
 *     <li>{@link #withArtifactDescriptor(String)}</li>
 *     <li>{@link #withArtifactIdentifier(String)}</li>
 *     <li>{@link #withArtifactVersion(String)}</li>
 *     <li>{@link #withArtifactVersion(Version)}</li>
 * </ul>
 *
 * <p><b>Build Phases</b></p>
 *
 * <ul>
 *     <li>{@link #disable(Phase)}</li>
 *     <li>{@link #enable(Phase)}</li>
 *     <li>{@link #isEnabled(Phase)}</li>
 *     <li>{@link #phase(String)}</li>
 *     <li>{@link #phases()}</li>
 * </ul>
 *
 *
 * <p><b>Dependencies</b></p>
 *
 * <ul>
 *     <li>{@link #dependencies()}</li>
 *     <li>{@link #pinVersion(Artifact, String)}</li>
 *     <li>{@link #pinVersion(Artifact, Version)}</li>
 *     <li>{@link #requires(Artifact, Artifact...)}</li>
 *     <li>{@link #requires(DependencyList)}</li>
 * </ul>
 *
 * <p><b>Build Metadata</b></p>
 *
 * <ul>
 *     <li>{@link #description()}</li>
 *     <li>{@link #metadata()}</li>
 *     <li>{@link #name()}</li>
 * </ul>
 *
 * <p><b>Build Structure</b></p>
 *
 * <ul>
 *     <li>{@link #sourceFolder()} - src</li>
 *     <li>{@link #sourceMainJavaFolder()} - src/main/java</li>
 *     <li>{@link #sourceMainResourcesFolder()} - src/main/resources</li>
 *     <li>{@link #sourceTestFolder()} - src/test</li>
 *     <li>{@link #sourceTestJavaFolder()} - src/test/java</li>
 *     <li>{@link #sourceTestResourcesFolder()} - src/test/resources</li>
 *     <li>{@link #targetClassesFolder()} - target/classes</li>
 *     <li>{@link #targetTestClassesFolder()} - target/test-classes</li>
 *     <li>{@link #sourceMainJavaSources()} - src/main/java/**.java</li>
 *     <li>{@link #sourceMainResources()} - src/main/resources/**</li>
 *     <li>{@link #sourceTestJavaSources()} - src/test/java/**.java</li>
 *     <li>{@link #sourceTestResources()} - src/test/resources/**</li>
 * </ul>
 *
 * <p><b>Build Environment</b></p>
 *
 * <ul>
 *     <li>{@link #isMac()}</li>
 *     <li>{@link #isUnix()}</li>
 *     <li>{@link #isWindows()}</li>
 *     <li>{@link #operatingSystemType()}</li>
 *     <li>{@link #environmentVariables()}</li>
 *     <li>{@link #property(String)}</li>
 *     <li>{@link #processorArchitecture()}</li>
 *     <li>{@link #processors()}</li>
 *     <li>{@link #javaHome()}</li>
 *     <li>{@link #mavenHome()}</li>
 * </ul>
 *
 * @author Jonathan Locke
 */
@SuppressWarnings("UnusedReturnValue")
public interface Build extends
    Repeater,
    Described,
    Named,
    Buildable,
    BuildStructured,
    BuildDependencies,
    BuildEnvironment,
    BuildArtifact,
    BuildPhased
{
    /**
     * Returns the child build at the given path
     *
     * @param path The child path
     * @return The child build
     */
    Build childBuild(String path);

    /**
     * Returns a copy of this build
     */
    Build copy();

    /**
     * Returns the metadata for this build
     */
    BuildMetadata metadata();

    /**
     * Returns a copy of this build with the given root folder
     *
     * @param root The new root folder
     * @return The copy
     */
    Build withRootFolder(Folder root);
}
