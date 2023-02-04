package digital.fiasco.runtime.build.builder.tools;

import com.telenav.kivakit.core.messaging.Repeater;
import digital.fiasco.runtime.build.builder.Builder;
import digital.fiasco.runtime.build.builder.BuilderAssociated;
import digital.fiasco.runtime.build.builder.tools.archiver.Archiver;
import digital.fiasco.runtime.build.builder.tools.cleaner.Cleaner;
import digital.fiasco.runtime.build.builder.tools.compiler.JavaCompiler;
import digital.fiasco.runtime.build.builder.tools.copier.Copier;
import digital.fiasco.runtime.build.builder.tools.git.Git;
import digital.fiasco.runtime.librarian.Librarian;
import digital.fiasco.runtime.librarian.RepositoryLibrarian;
import digital.fiasco.runtime.build.builder.tools.shader.Shader;
import digital.fiasco.runtime.build.builder.tools.stamper.BuildStamper;
import digital.fiasco.runtime.build.builder.tools.tester.Tester;
import digital.fiasco.runtime.build.environment.BuildStructure;

import static com.telenav.kivakit.core.language.Classes.newInstance;

/**
 * @author Jonathan Locke
 */
@SuppressWarnings("unused")
public interface ToolFactory extends
    Repeater,
    BuilderAssociated,
    BuildStructure
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
     * Creates a new {@link JavaCompiler} tool
     */
    default JavaCompiler newCompiler()
    {
        return newTool(JavaCompiler.class);
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
     * Creates a new {@link RepositoryLibrarian} tool
     */
    default Librarian newLibrarian()
    {
        return newTool(RepositoryLibrarian.class);
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

    default <T extends Tool<T>> T newTool(Class<T> type)
    {
        return listenTo(newInstance(type, Builder.class, associatedBuilder()));
    }
}
