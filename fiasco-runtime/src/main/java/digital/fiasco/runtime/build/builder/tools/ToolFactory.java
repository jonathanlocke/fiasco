package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.language.Classes;
import com.telenav.kivakit.core.messaging.Repeater;
import digital.fiasco.runtime.build.builder.BuildStructured;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.BuilderAssociated;
import digital.fiasco.runtime.build.builder.tools.archiver.Archiver;
import digital.fiasco.runtime.build.builder.tools.cleaner.Cleaner;
import digital.fiasco.runtime.build.builder.tools.compiler.Compiler;
import digital.fiasco.runtime.build.builder.tools.copier.Copier;
import digital.fiasco.runtime.build.builder.tools.git.Git;
import digital.fiasco.runtime.build.builder.tools.librarian.Librarian;
import digital.fiasco.runtime.build.builder.tools.shader.Shader;
import digital.fiasco.runtime.build.builder.tools.stamper.BuildStamper;
import digital.fiasco.runtime.build.builder.tools.tester.Tester;

/**
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface ToolFactory extends
    Repeater,
    BuilderAssociated,
    BuildStructured
{
    /**
     * Creates a new {@link Archiver} tool
     */
    default Archiver newArchiver()
    {
        return newTool(Archiver.class);
    }

    /**
     * Creates a new {@link Cleaner} tool
     */
    default Cleaner newCleaner()
    {
        return newTool(Cleaner.class);
    }

    /**
     * Creates a new {@link Compiler} tool
     */
    default Compiler newCompiler()
    {
        return newTool(Compiler.class);
    }

    /**
     * Creates a new {@link Copier} tool
     */
    default Copier newCopier()
    {
        return newTool(Copier.class);
    }

    /**
     * Creates a new {@link Git} tool
     */
    default Git newGit()
    {
        return newTool(Git.class);
    }

    /**
     * Creates a new {@link Librarian} tool
     */
    default Librarian newLibrarian()
    {
        return newTool(Librarian.class);
    }

    /**
     * Creates a new {@link Shader} tool
     */
    default Shader newShader()
    {
        return newTool(Shader.class);
    }

    /**
     * Creates a new {@link BuildStamper} tool
     */
    default BuildStamper newStamper()
    {
        return newTool(BuildStamper.class);
    }

    /**
     * Creates a new {@link Tester} tool
     */
    default Tester newTester()
    {
        return newTool(Tester.class);
    }

    default <T extends Tool> T newTool(Class<T> type)
    {
        return listenTo(Classes.newInstance(type, Builder.class, associatedBuilder()));
    }
}
