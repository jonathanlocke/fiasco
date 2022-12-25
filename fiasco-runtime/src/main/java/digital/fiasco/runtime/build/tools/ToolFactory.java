package digital.fiasco.runtime.build.tools;

import digital.fiasco.runtime.build.BuildAssociated;
import digital.fiasco.runtime.build.BuildStructured;
import digital.fiasco.runtime.build.tools.archiver.Archiver;
import digital.fiasco.runtime.build.tools.builder.Builder;
import digital.fiasco.runtime.build.tools.cleaner.Cleaner;
import digital.fiasco.runtime.build.tools.compiler.Compiler;
import digital.fiasco.runtime.build.tools.copier.Copier;
import digital.fiasco.runtime.build.tools.git.Git;
import digital.fiasco.runtime.build.tools.librarian.Librarian;
import digital.fiasco.runtime.build.tools.shader.Shader;
import digital.fiasco.runtime.build.tools.stamper.BuildStamper;
import digital.fiasco.runtime.build.tools.tester.Tester;

import static com.telenav.kivakit.core.version.Version.version;

/**
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface ToolFactory extends
        BuildAssociated,
        BuildStructured
{
    /**
     * Creates a new {@link Archiver} tool
     */
    default Archiver newArchiver()
    {
        return new Archiver(associatedBuild());
    }

    /**
     * Creates a new {@link Builder} tool
     */
    default Builder newBuilder()
    {
        return new Builder(associatedBuild());
    }

    /**
     * Creates a new {@link Cleaner} tool
     */
    default Cleaner newCleaner()
    {
        return new Cleaner(associatedBuild());
    }

    /**
     * Creates a new {@link Compiler} tool, with default Java sources and source/target versions.
     */
    default Compiler newCompiler()
    {
        return new Compiler(associatedBuild())
                .withSources(sourceMainJavaSources())
                .withSourceVersion(version(17))
                .withTargetVersion(version(17));
    }

    /**
     * Creates a new {@link Copier} tool
     */
    default Copier newCopier()
    {
        return new Copier(associatedBuild());
    }

    /**
     * Creates a new {@link Git} tool
     */
    default Git newGit()
    {
        return new Git(associatedBuild());
    }

    /**
     * Creates a new {@link Librarian} tool
     */
    default Librarian newLibrarian()
    {
        return new Librarian(associatedBuild());
    }

    /**
     * Creates a new {@link Shader} tool
     */
    default Shader newShader()
    {
        return new Shader(associatedBuild());
    }

    /**
     * Creates a new {@link BuildStamper} tool
     */
    default BuildStamper newStamper()
    {
        return new BuildStamper(associatedBuild());
    }

    /**
     * Creates a new {@link Tester} tool
     */
    default Tester newTester()
    {
        return new Tester(associatedBuild());
    }
}
