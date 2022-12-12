package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.core.messaging.Repeater;
import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import digital.fiasco.runtime.build.BuildAssociated;
import digital.fiasco.runtime.build.tools.archiver.Archiver;
import digital.fiasco.runtime.build.tools.cleaner.Cleaner;
import digital.fiasco.runtime.build.tools.compiler.Compiler;
import digital.fiasco.runtime.build.tools.copier.Copier;
import digital.fiasco.runtime.build.tools.git.Git;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.build.tools.shader.Shader;
import digital.fiasco.runtime.build.tools.stamper.BuildStamper;
import digital.fiasco.runtime.build.tools.tester.Tester;

import static com.telenav.kivakit.core.version.Version.version;
import static com.telenav.kivakit.resource.ResourceGlob.glob;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface ToolFactory extends
        Repeater,
        BuildAssociated
{
    default Archiver archiver()
    {
        return new Archiver(associatedBuild());
    }

    default BuildStamper buildStamper()
    {
        return new BuildStamper(associatedBuild());
    }

    default Cleaner cleaner(Folder folder, String glob)
    {
        return cleaner()
                .withFolder(folder)
                .withMatcher(at -> folder.nestedResources(glob(glob)).contains((Resource) at));
    }

    default Cleaner cleaner()
    {
        return new Cleaner(associatedBuild());
    }

    default Compiler compiler()
    {
        return new Compiler(associatedBuild())
                .sources(associatedBuild().javaSources())
                .sourceVersion(version("17"))
                .targetVersion(version("17"));
    }

    default Copier copier()
    {
        return new Copier(associatedBuild());
    }

    default Git git()
    {
        return new Git(associatedBuild());
    }

    default Shader shader()
    {
        return new Shader(associatedBuild());
    }

    default Tester tester()
    {
        return new Tester(associatedBuild());
    }
}
