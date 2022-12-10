package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import digital.fiasco.runtime.build.Buildable;
import digital.fiasco.runtime.build.resources.ResourceGlobbing;
import digital.fiasco.runtime.build.tools.archiver.Archiver;
import digital.fiasco.runtime.build.tools.cleaner.Cleaner;
import digital.fiasco.runtime.build.tools.compiler.Compiler;
import digital.fiasco.runtime.build.tools.copier.Copier;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.build.tools.shader.Shader;
import digital.fiasco.runtime.build.tools.tester.Tester;

import static com.telenav.kivakit.core.version.Version.version;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface ToolFactory extends Buildable, ResourceGlobbing
{
    default Archiver archiver()
    {
        return new Archiver(baseBuild());
    }

    default Cleaner cleaner(Folder folder, String glob)
    {
        return new Cleaner(baseBuild()).matching(at ->
                folder.nestedResources(glob).contains((Resource) at));
    }

    default Compiler compiler()
    {
        return new Compiler(baseBuild())
                .sources(baseBuild().javaSources())
                .sourceVersion(version("17"))
                .targetVersion(version("17"));
    }

    default Copier copier()
    {
        return new Copier(baseBuild());
    }

    default Librarian librarian()
    {
        return new Librarian(baseBuild());
    }

    default Shader shader()
    {
        return new Shader(baseBuild());
    }

    default Tester tester()
    {
        return new Tester(baseBuild());
    }
}
