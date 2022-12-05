package fiasco;

import com.telenav.kivakit.filesystem.Folder;
import fiasco.glob.Glob;
import fiasco.tools.archiver.Archiver;
import fiasco.tools.builder.Builder;
import fiasco.tools.cleaner.Cleaner;
import fiasco.tools.compiler.Compiler;
import fiasco.tools.copier.Copier;
import fiasco.tools.librarian.Librarian;
import fiasco.tools.shader.Shader;
import fiasco.tools.tester.Tester;

import static com.telenav.kivakit.core.version.Version.version;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface ToolFactory extends BuildAttached, Glob
{
    default Archiver archiver()
    {
        return new Archiver(baseBuild());
    }

    default Builder builder()
    {
        return new Builder(baseBuild());
    }

    default Cleaner cleaner(Folder folder, String glob)
    {
        return new Cleaner(baseBuild()).matching(glob(folder, glob));
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
