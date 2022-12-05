package fiasco.tools;

import fiasco.BaseBuildSource;
import fiasco.tools.archiver.Archiver;
import fiasco.tools.builder.Builder;
import fiasco.tools.compiler.Compiler;
import fiasco.tools.copier.Copier;
import fiasco.tools.librarian.Librarian;
import fiasco.tools.shader.Shader;
import fiasco.tools.tester.Tester;

/**
 * @author jonathan
 */
@SuppressWarnings("unused")
public interface Tools extends BaseBuildSource
{
    default Archiver archiver()
    {
        return new Archiver(this);
    }

    default Builder builder()
    {
        return new Builder(baseBuild());
    }

    default Compiler compiler()
    {
        return new Compiler(baseBuild());
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
