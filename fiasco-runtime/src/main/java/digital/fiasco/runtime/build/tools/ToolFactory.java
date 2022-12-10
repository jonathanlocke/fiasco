package digital.fiasco.runtime.build.tools;

import com.telenav.kivakit.filesystem.Folder;
import com.telenav.kivakit.resource.Resource;
import digital.fiasco.runtime.build.Built;
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
public interface ToolFactory extends Built, ResourceGlobbing
{
    default Archiver archiver()
    {
        return new Archiver(build());
    }

    default Cleaner cleaner(Folder folder, String glob)
    {
        return new Cleaner(build()).matching(at ->
                folder.nestedResources(glob).contains((Resource) at));
    }

    default Compiler compiler()
    {
        return new Compiler(build())
                .sources(build().javaSources())
                .sourceVersion(version("17"))
                .targetVersion(version("17"));
    }

    default Copier copier()
    {
        return new Copier(build());
    }

    default Librarian librarian()
    {
        return new Librarian(build());
    }

    default Shader shader()
    {
        return new Shader(build());
    }

    default Tester tester()
    {
        return new Tester(build());
    }
}
