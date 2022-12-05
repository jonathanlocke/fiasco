package fiasco.tools;

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
public interface Tools
{
    default Archiver archiver()
    {
        return new Archiver(this);
    }

    default Builder builder()
    {
        return new Builder(this);
    }

    default Compiler compiler()
    {
        return new Compiler(this);
    }

    default Copier copier()
    {
        return new Copier(this);
    }

    default Librarian librarian()
    {
        return new Librarian(this);
    }

    default Shader shader()
    {
        return new Shader(this);
    }

    default Tester tester()
    {
        return new Tester(this);
    }
}
